/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils;

import com.cobalt.itidez.derputils.achievements.AchivementManager;
import com.cobalt.itidez.derputils.commands.Command;
import com.cobalt.itidez.derputils.commands.CommandArgs;
import com.cobalt.itidez.derputils.commands.CommandFramework;
import com.cobalt.itidez.derputils.config.Config;
import com.cobalt.itidez.derputils.config.ConfigManager;
import com.cobalt.itidez.derputils.listeners.PlayerListener;
import com.cobalt.itidez.derputils.messageutils.ImageChar;
import com.cobalt.itidez.derputils.messageutils.ImageMessage;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author iTidez
 */
public class DerpUtils extends JavaPlugin {
    public CommandFramework framework;
    private AchivementManager achievementManager;
    private static DerpUtils instance;
    private boolean debug;
    private Config utilsConfig;
    private Logger log;
    
    @Override
    public void onEnable() {
        this.instance = this;
        this.log = Logger.getLogger("Minecraft");
        this.utilsConfig = ConfigManager.createConfig(getDataFolder(), "config.yml");
        updateConfig();
        this.debug = (boolean)utilsConfig.getValue("debug");
        this.achievementManager = new AchivementManager();
        this.framework = new CommandFramework(this);
        this.framework.registerCommands(this);
        this.framework.registerCommands(achievementManager);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        
    }
    
    private void updateConfig() {
        if(!utilsConfig.hasValue("debug")) {
            utilsConfig.setValue("debug", false);
        }
    }
    
    @Override
    public void onDisable() {
        this.instance = null;
    }
    
    @Command(name="imgmsg", permission="derp.imgmsg")
    public void onImgMsg(CommandArgs arg) {
        BufferedImage image = null;
        try {
            URL url = new URL(arg.getArgs(1));
            image = ImageIO.read(url);
            ImageMessage im = new ImageMessage(image, 10, ImageChar.MEDIUM_SHADE.getChar());
            im.sendToPlayer(Bukkit.getPlayer(arg.getArgs(0)));
        } catch(Exception ex) {
            
        }
    }
    
    public AchivementManager getAchivementManager() {
        return achievementManager;
    }
    
    public static DerpUtils getInstance() {
        return instance;
    }
    
    public void log(String message) {
        log.log(Level.INFO, "[DerpUtils] {0}", message);
    }
    
    public void debug(String message) {
        if(debug)
            log("[DEBUG] "+message);
    }
}
