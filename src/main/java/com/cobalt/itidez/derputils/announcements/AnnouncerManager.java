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
import org.bukkit.entity.Player;

/**
 *
 * @author iTidez
 */
public class AnnouncerManager {
    private Config announcements;
    
    public AnnouncerManager() {
        announcements = ConfigManager.createConfig(DerpUtils.getInstance().getDataFolder(), "announcements.yml");
        checkDefaults();
    }
    
    private void checkDefaults() {
        if(!announcements.hasValue("messages")) {
            List<String> messages = new ArrayList<>();
            messages.add("This is a test message!");
            announcements.setValue("messages", messages);
        }
    }
    
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
    }
    
    @Command(name="announce.reload", aliases={"rd"}, permission="derp.announce.reload")
    public void onAnnounceReload(CommandArgs args) {
        announcements = ConfigManager.createConfig(DerpUtils.getInstance().getDataFolder(), "announcements.yml");
    }
}
