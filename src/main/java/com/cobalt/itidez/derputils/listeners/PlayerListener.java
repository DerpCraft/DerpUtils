/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.listeners;

import com.cobalt.itidez.derputils.DerpUtils;
import com.cobalt.itidez.derputils.achievements.AchivementManager;
import com.cobalt.itidez.derputils.config.Config;
import com.cobalt.itidez.derputils.config.ConfigManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 *
 * @author iTidez
 */
public class PlayerListener implements Listener {
    private Config joinedPlayers;
    private List<String> listPlayers;
    
    public PlayerListener() {
        joinedPlayers = ConfigManager.createConfig(DerpUtils.getInstance().getDataFolder(), "players.yml");
        listPlayers = new ArrayList<>();
        if(joinedPlayers.hasValue("player")) {
            listPlayers = (List<String>)joinedPlayers.getValue("player");
        } else {
            joinedPlayers.setValue("player", listPlayers);
        }
    }
    
    public void addPlayerToList(Player p) {
        listPlayers.add(p.getName());
        joinedPlayers.setValue("player", listPlayers);
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        AchivementManager am = DerpUtils.getInstance().getAchivementManager();
        if(listPlayers.contains(event.getPlayer().getName()))
            am.sendNotification(event.getPlayer(), "Welcome back", "to Derp Craft!", null);
        else {
            am.sendNotification(event.getPlayer(), "Welcome to Derp Craft!", "Visit our site @ derpcraft.co", "http://derpcraft.co/");
            addPlayerToList(event.getPlayer());
        }
    }
    
}
