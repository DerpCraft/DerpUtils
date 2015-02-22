/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils;

import com.cobalt.itidez.derputils.achievements.AchivementManager;
import com.cobalt.itidez.derputils.commands.Command;
import com.cobalt.itidez.derputils.commands.CommandArgs;
import com.cobalt.itidez.derputils.commands.CommandFramework;
import com.cobalt.itidez.derputils.commands.SendItemCommand;
import com.cobalt.itidez.derputils.config.Config;
import com.cobalt.itidez.derputils.config.ConfigManager;
import com.cobalt.itidez.derputils.listeners.PlayerListener;
import com.cobalt.itidez.derputils.messages.ActionManager;
import com.cobalt.itidez.derputils.messages.ActionbarAnnouncement;
import com.cobalt.itidez.derputils.messages.EntityListener;
import com.cobalt.itidez.derputils.messages.MessageManager;
import com.cobalt.itidez.derputils.messages.StaffRequests;
import com.cobalt.itidez.derputils.messageutils.ImageChar;
import com.cobalt.itidez.derputils.messageutils.ImageMessage;
import com.cobalt.itidez.derputils.prefixes.Tablist;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.milkbowl.vault.permission.Permission;
import ninja.amp.ampmenus.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author iTidez
 */
public class DerpUtils extends JavaPlugin {
    public CommandFramework framework;
    private AchivementManager achievementManager;
    //private AnnouncerManager announcements;
    private static DerpUtils instance;
    private boolean debug;
    private Config utilsConfig;
    private Logger log;
    private Permission permission;
    public MessageManager mm;
    public ActionManager aa;
    public StaffRequests sr;
    
    @Override
    public void onEnable() {
        DerpUtils.instance = this;
        
        this.aa = new ActionManager();
        this.log = Logger.getLogger("Minecraft");
        this.utilsConfig = ConfigManager.createConfig(getDataFolder(), "config.yml");
        updateConfig();
        this.debug = (boolean)utilsConfig.getValue("debug");
        this.achievementManager = new AchivementManager();
        //this.announcements = new AnnouncerManager();
        this.framework = new CommandFramework(this);
        setupPermissions();
        sr = new StaffRequests();
        mm = new MessageManager();
        for(Player p : Bukkit.getOnlinePlayers()) {
            log(p.getName()+" is registered for messages");
            mm.beginTimerForPlayer(p);
            sr.beginTimerForPlayer(p);
        }
        
        MenuListener.getInstance().register(this);
        
        this.framework.registerCommands(this);
        this.framework.registerCommands(mm);
        this.framework.registerCommands(achievementManager);
        this.framework.registerCommands(new Tablist());
        this.framework.registerCommands(sr);
        this.framework.registerCommands(new SendItemCommand());
        //this.framework.registerCommands(announcements);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        //Pex p = new Pex();
        //TODO: Add NPC's
        
        updatePlayerTablist();
    }
    
    public Permission getPermissions() {
        return permission;
    }
    
    public Config getUtilsConfig() {
        return utilsConfig;
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if(permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
    
    private void updateConfig() {
        if(!utilsConfig.hasValue("debug")) {
            utilsConfig.setValue("debug", false);
        }
        /*String[] groups = Pex.getGroups();
        for(String g : groups) {
            if(!utilsConfig.hasValue("groups."+g))
                utilsConfig.setValue("groups."+g, "white");
        }*/
    }
    
    @Override
    public void onDisable() {
        DerpUtils.instance = null;
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
    
    @Command(name="uuid", permission="derp.uuid")
    public void onUUIDRequest(CommandArgs args) {
        args.getPlayer().sendMessage(Bukkit.getOfflinePlayer(args.getArgs(0)).getUniqueId().toString());
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
    
    public int getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().length;
    }
    
    public void updatePlayerTablist() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            
        //Teams.addColor(event.getPlayer(), (String)DerpUtils.getInstance().getUtilsConfig().getValue("groups."+playerGroup));
            if(DerpUtils.getInstance().getUtilsConfig().hasValue("users."+player.getName()) && !(DerpUtils.getInstance().getUtilsConfig().getValue("users."+player.getName()).equals("none"))) {
                Tablist.apply(player);
            } else {
                String playerGroup = Pex.getPlayerGroup(player);
                Tablist.apply(player, playerGroup);
            }
        }
    }
}
