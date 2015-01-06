/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * @author iTidez
 */
public class PluginCommand implements Listener {
    
    public PluginCommand(DerpUtils pl) {
        Bukkit.getPluginManager().registerEvents(this, pl);
    }
    
    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        Player p = event.getPlayer();
        List<String> overrideCommands = new ArrayList<>();
        overrideCommands.add("/pl");
        overrideCommands.add("/plugins");
        if(overrideCommands.contains(command)) {
            //if(!p.hasPermission("derp.pluginlist")) {
                event.setCancelled(true);
                List<String> plugins = new ArrayList<>();
                plugins.add("DerpAnnouncer");
                plugins.add("DerpHolograms");
                plugins.add("DerpNotifier");
                plugins.add("DerpShout");
                plugins.add("DerpUtilities");
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.WHITE+"Plugins (5): ");
                for(String s : plugins) {
                    builder.append(ChatColor.GREEN+s+ChatColor.WHITE+", ");
                }
                String message = builder.toString().substring(0, builder.length() - 2);
            //}
        }
    }
}
