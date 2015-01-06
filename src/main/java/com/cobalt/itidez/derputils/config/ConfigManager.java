/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.config;

import java.io.File;
import java.util.HashMap;

 
/**
 *
 * @author iTidez
 */
public class ConfigManager
{
    private static final HashMap<String, Config> configFiles = new HashMap<String, Config>();
 
    /**
     * Create a configuration file. Creates the file, adds it to the registry, and returns the new instance
     * @param folder Folder to store the config file in
     * @param name Name of file (NOTE: Include file extension!)
     * @return The new Config instance.
     */
    public static Config createConfig(File folder, String name)
    {
        Config cfg = new Config(folder, name);
 
        if(!configFiles.containsKey(name))
        {
            if(cfg.create())
            {
                configFiles.put(name, cfg);
                return cfg;
            }
            else
            {
                System.err.println("Error: Could not create file.");
                return null;
            }
        }
        else
        {
            return getConfig(name);
        }
    }
 
    /**
     * Gets the config from the registry.
     * @param name Name of config (Checks through config database, throws an InvalidConfigException if the inputted config isn't registered)
     * @return The config selected from the registry.
     */
    public static Config getConfig(String name)
    {
        if(configFiles.containsKey(name))
        {
            return configFiles.get(name);
        }
        else
        {
            throw new InvalidConfigException("Could not locate file " + name + "! (Was not registered)");
        }
    }
    
    public static boolean load(File folder, String name) {
        Config cfg = new Config(folder, name);
        if(cfg.getFile().exists()) {
            configFiles.put(name, cfg);
            return true;
        } else {
            cfg.create();
            configFiles.put(name, cfg);
            return false;
        }
    }
    
    public static boolean configExists(String name) {
        return configFiles.containsKey(name);
    }
 
    /**
     * Deletes config's file and registry.
     * @param name Name of config (Checks through config database, throws an InvalidConfigException if the inputted config isn't registered)
     * @return True if deleted successfully, false if something went wrong.
     */
    public static boolean deleteConfig(String name)
    {
        if(configFiles.containsKey(name))
        {
            Config cfg = configFiles.get(name);
 
            if(cfg.delete())
            {
                configFiles.remove(name);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            throw new InvalidConfigException("Could not delete file " + name + "! (Was not registered)");
        }
    }
 
    /**
     * Recreate a config (In the event of data corruption, updated cfgs, etc.)
     * @param name Name of config (Checks through config database, throws an InvalidConfigException if the inputted config isn't registered)
     * @return True if recreated, false if something went wrong.
     */
    public static boolean recreateConfig(String name)
    {
        if(configFiles.containsKey(name))
        {
            Config cfg = configFiles.get(name);
 
            return cfg.recreate();
        }
        else
        {
            throw new InvalidConfigException("Could not find file " + name + "! (Was not registered)");
        }
    }
 
    /**
     * Save all currently registered configurations.
     */
    public static void saveAllConfigs()
    {
        for(Config cfg : configFiles.values())
        {
            cfg.save();
        }
    }
 
    /**
     * Use in your plugin's onDisable method. Just a precaution, to save all the files.
     */
    public static void onDisable()
    {
        for(Config cfg : configFiles.values())
        {
            cfg.save();
        }
        configFiles.clear();
    }
}
/**
 * Exception that's thrown in the event an invalid configuration is used/found.
 * @author Hydroxocobalamin
 */
class InvalidConfigException extends RuntimeException
{
    public InvalidConfigException(String ex)
    {
        super(ex);
    }
}
