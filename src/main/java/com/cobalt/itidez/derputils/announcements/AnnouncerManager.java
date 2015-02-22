/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cobalt.itidez.derputils.announcements;

import com.cobalt.itidez.derputils.DerpUtils;
import com.cobalt.itidez.derputils.commands.Command;
import com.cobalt.itidez.derputils.commands.CommandArgs;
import com.cobalt.itidez.derputils.config.Config;
import com.cobalt.itidez.derputils.config.ConfigManager;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author iTidez
 */
public class AnnouncerManager {
    private Config announcements;
    private List<String> messages;
    private boolean enabled;
    private static int index;
    
    public AnnouncerManager() {
        announcements = ConfigManager.createConfig(DerpUtils.getInstance().getDataFolder(), "announcements.yml");
        messages = new ArrayList<>();
        enabled = true;
        index = -1;
        checkDefaults();
        startAnnouncements();
    }
    
    private void checkDefaults() {
        if(!announcements.hasValue("messages")) {
            messages.add("This is a test message!");
            announcements.setValue("messages", messages);
        } else {
            messages = (List<String>)announcements.getValue("messages");
        }
    }
    
    public void startAnnouncements() {
        enabled = true;
        announce();
    }
    
    public void stopAnnouncements() {
        enabled = false;
    }
    
    public void announce() {
        if(!announcements.hasValue("interval")) {
            announcements.setValue("interval", 90);
        }
        long interval = 20 * (long)announcements.getValue("interval");
        Bukkit.getScheduler().scheduleSyncDelayedTask(DerpUtils.getInstance(), new Runnable() {
            @Override
            public void run() {
                int i = AnnouncerManager.incrementIndex();
                Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"["+
                        ChatColor.BLUE+"Derp Announcement"+ChatColor.DARK_PURPLE+"] "
                        +ChatColor.BLUE+ChatColor.translateAlternateColorCodes('&', messages.get(i)));
                if(!enabled)
                    Bukkit.getScheduler().cancelTasks(DerpUtils.getInstance());
            }
        }, interval);
    }
    
    public static int incrementIndex() {
        return index++;
    }
    
    /**
     * Begin Command Declarations
     */
    
    
    @Command(name="announce")
    public void onAnnounce(CommandArgs args) {
        onAnnounceHelp(args);
    }
    
    @Command(name="announce.help", aliases = {"h, i"})
    public void onAnnounceHelp(CommandArgs args) {
        Player p = args.getPlayer();
        p.sendMessage(ChatColor.DARK_PURPLE+"-------"+ChatColor.DARK_AQUA+" DC Announcer "+ChatColor.DARK_PURPLE+"-------");
        p.sendMessage(ChatColor.DARK_AQUA+"  - help, h : Shows this window");
        if(p.hasPermission("derp.announce.list")) {
            p.sendMessage(ChatColor.DARK_AQUA+"  - list, l, li : Shows all active/inactive messages");
        }
        if(p.hasPermission("derp.announce.add")) {
            p.sendMessage(ChatColor.DARK_AQUA+"  - add, a : Adds a message to the active messages");
        }
        if(p.hasPermission("derp.announce.remove")) {
            p.sendMessage(ChatColor.DARK_AQUA+"  - remove, rm : Removes a message from the active messages");
        }
        if(p.hasPermission("derp.announce.reload")) {
            p.sendMessage(ChatColor.DARK_AQUA+"  - reload, rd : Reloads the config");
        }
        if(p.hasPermission("derp.announce.enable")) {
            p.sendMessage(ChatColor.DARK_AQUA+"  - on : Starts the announcer");
            p.sendMessage(ChatColor.DARK_AQUA+"  - off : Stops the announcer");
        }
    }
    
    @Command(name="announce.reload", aliases={"rd"}, permission="derp.announce.reload")
    public void onAnnounceReload(CommandArgs args) {
        announcements = ConfigManager.createConfig(DerpUtils.getInstance().getDataFolder(), "announcements.yml");
    }
    
    @Command(name="announce.add", aliases={"a"}, permission="derp.announce.add")
    public void onAnnounceAdd(CommandArgs args) {
        StringBuilder finalMessage = new StringBuilder();
        String[] preMessage = args.getArgs();
        for(String m : preMessage) {
            finalMessage.append(m);
        }
        messages.add(finalMessage.toString());
        announcements.setValue("messages", messages);
        args.getPlayer().sendMessage(ChatColor.GREEN+"Successfully added the following announcement:");
        args.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage.toString()));
    }
    
    @Command(name="announce.remove", aliases={"rm"}, permission="derp.announce.remove")
    public void onAnnounceRemove(CommandArgs args) {
        int index = 0;
        try {
            index = Integer.parseInt(args.getArgs()[0]);
            index--;
        } catch(Exception e) {
            args.getPlayer().sendMessage(ChatColor.RED+"Error: You must specify the index of the announcement to delete!");
            args.getPlayer().sendMessage(ChatColor.RED+"Please check /announce list for the corresponding index");
        }
        
        if(messages.size() > index) {
            args.getPlayer().sendMessage(ChatColor.RED+"Error: Index"+index+" is not located in the announcement list");
            args.getPlayer().sendMessage(ChatColor.RED+"Please re-check your number with /announce list");
        } else {
            String toDelete = messages.get(index);
            messages.remove(index);
            args.getPlayer().sendMessage(ChatColor.GREEN+"Successfully removed the following announcement:");
            args.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', toDelete));
            announcements.setValue("messages", messages);
        }
    }
    
    @Command(name="announce.list", aliases={"li, l"}, permission="derp.announce.list")
    public void onAnnounceList(CommandArgs args) {
        Player p = args.getPlayer();
        p.sendMessage(ChatColor.DARK_PURPLE+"---- "+ChatColor.DARK_AQUA+"Announcements"+ChatColor.DARK_PURPLE+" ----");
        int i = 1;
        for(String s : messages) {
            p.sendMessage(ChatColor.DARK_BLUE+"  ["+i+"] "+ChatColor.WHITE+ChatColor.translateAlternateColorCodes('&', s));
        }
    }
    
    @Command(name="announce.on", permission="derp.announce.enable")
    public void onAnnounceOn(CommandArgs args) {
        startAnnouncements();
        args.getPlayer().sendMessage(ChatColor.GREEN+"Turned on announcements");
    }
    
    @Command(name="announce.off",  permission="derp.announce.enable")
    public void onAnnounceOff(CommandArgs args) {
        stopAnnouncements();
        args.getPlayer().sendMessage(ChatColor.GREEN+"Turned off announcements");
    }
}
