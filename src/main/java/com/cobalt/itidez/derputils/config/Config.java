/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.config;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author iTidez
 */
public final class Config
{
    private File configFile;
    private YamlConfiguration config;
    private String fileName;
 
    /**
     * Default constructor for configurations.
     * @param folder Folder to store the config in
     * @param name Name of the file (Specify extension!)
     */
    public Config(File folder, String name)
    {
        fileName = name;
        configFile = new File(folder, name);
    }
 
    /**
     * Get the config file
     * @return config file
     */
    public File getFile()
    {
        return configFile;
    }
 
    /**
     * Get the YamlConfiguration paired with the config
     * @return The config
     */
    public YamlConfiguration getYaml()
    {
        return config;
    }
 
    /**
     * Get the name of the file
     * @return File name
     */
    public String getFileName()
    {
        return fileName;
    }
 
    /**
     * Get the folder the config is stored in
     * @return The config's folder
     */
    public File getFolder()
    {
        return configFile.getParentFile();
    }
 
    /**
     * Create a configuration section
     * @param path Section to create
     */
    public void createSection(String path)
    {
        if(!config.contains(path))
        {
            config.createSection(path);
            save();
        }
    }
 
    /**
     * Set the value to the config file.
     * @param path Path to store the variable
     * @param value Value to store on the path
     * @return The object specified
     */
    public Object setValue(String path, Object value)
    {
        if(hasValue(path, true))
        {
            config.set(path, value);
        }
        else
        {
            config.set(path, value);
        }
        save();
        return value;
    }
 
    /**
     * Check the config for a value
     * @param path Path to find the variable
     * @return Does the config have the value?
     */
    public boolean hasValue(String path)
    {
        if(config.contains(path))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
 
    /**
     * Check the config for a value
     * @param path Path to find the variable
     * @param createIfNot If it can't find the path, create it?
     * @return Does the config have the value?
     */
    public boolean hasValue(String path, boolean createIfNot)
    {
        if(config.contains(path))
        {
            return true;
        }
        else
        {
            if(createIfNot)
            {
                config.createSection(path);
                save();
            }
            return false;
        }
    }
 
    /**
     * Get an object from config, return null if it couldn't find the value
     * @param path Path to find the value
     * @return Config collected from file
     */
    public Object getValue(String path)
    {
        if(config.contains(path))
        {
            return config.get(path);
        }
        else
        {
            config.createSection(path);
            save();
        }
        return null;
    }
 
    /**
     * Get an object from config, return default if it couldn't find the path
     * @param path Path to find the value
     * @param def Default value, if it couldn't find one in the config
     * @return Config collected from file
     */
    public Object getValue(String path, Object def)
    {
        if(config.contains(path))
        {
            return config.get(path);
        }
        else
        {
            config.createSection(path);
            config.set(path, def);
            save();
            return def;
        }
    }
 
    /**
     * Create the config file, and folder.
     * @return True if it was created, false if an exception was thrown.
     */
    public boolean create()
    {
        try
        {
            if(!getFolder().exists())
            {
                getFolder().mkdir();
            }
 
            configFile.createNewFile();
            config = YamlConfiguration.loadConfiguration(configFile);
            return true;
        }
        catch(IOException ex)
        {
            System.err.println("Error while creating file (IOException): ");
            ex.printStackTrace();
            return false;
        }
    }
 
    /**
     * Delete the config, then re-create it.
     * @return True if it worked, false if an exception was thrown.
     */
    public boolean recreate()
    {
        configFile.delete();
        config = null;
 
        try
        {
            if(!getFolder().exists())
            {
                getFolder().mkdir();
            }
 
            configFile.createNewFile();
            config = YamlConfiguration.loadConfiguration(configFile);
            return true;
        }
        catch (IOException ex)
        {
            System.err.println("Error while creating file (IOException): ");
            ex.printStackTrace();
            return false;
        }
    }
 
    /**
     * Delete the config file.
     * @return True or false, depending on if it was deleted or not.
     */
    public boolean delete()
    {
        return configFile.delete();
    }
 
    /**
     * Delete the folder the config is stored in.
     * @return True or false, depending on if it was deleted or not.
     */
    public boolean deleteFolder()
    {
        return getFolder().delete();
    }
 
 
    /**
     * Save the config to disk.
     * @return True if it saved, false if an exception was thrown
     */
    public boolean save()
    {
        try
        {
            config.save(configFile);
            return true;
        }
        catch(IOException ex)
        {
            return false;
        }
    }
}
