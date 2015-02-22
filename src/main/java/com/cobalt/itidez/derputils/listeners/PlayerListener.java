/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.listeners;

import com.cobalt.itidez.derputils.DerpUtils;
import com.cobalt.itidez.derputils.Pex;
import com.cobalt.itidez.derputils.achievements.AchivementManager;
import com.cobalt.itidez.derputils.config.Config;
import com.cobalt.itidez.derputils.config.ConfigManager;
import com.cobalt.itidez.derputils.prefixes.Tablist;
import io.puharesource.mc.titlemanager.api.TitleObject;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        try {
        final Player pl = event.getPlayer();
        AchivementManager am = DerpUtils.getInstance().getAchivementManager();
        if(listPlayers.contains(event.getPlayer().getName()))
            //am.sendNotification(event.getPlayer(), "Welcome back", "to Derp Craft!", null);
            sendFloatingText(event.getPlayer(), ChatColor.YELLOW+"Welcome back to", ChatColor.YELLOW+"Derp craft", 20, 50, 20);
        else {
            //am.sendNotification(event.getPlayer(), "Welcome to Derp Craft!", "Visit our site @ derpcraft.co", "http://derpcraft.co");
            sendFloatingText(event.getPlayer(), ChatColor.YELLOW+"Welcome to", ChatColor.YELLOW+"Derp Craft", 20, 50, 20);
            Bukkit.getScheduler().runTaskLater(DerpUtils.getInstance(), new Runnable() {
                @Override
                public void run() {
                    sendFloatingText(pl, ChatColor.YELLOW+"Make sure to go to", ChatColor.YELLOW+"http://derpcraft.co", 20, 50, 20);
                }
            }, 70);
            addPlayerToList(event.getPlayer());
        }
        
        //Teams.addColor(event.getPlayer(), (String)DerpUtils.getInstance().getUtilsConfig().getValue("groups."+playerGroup));
        if(DerpUtils.getInstance().getUtilsConfig().hasValue("users."+event.getPlayer().getName()) && !(DerpUtils.getInstance().getUtilsConfig().getValue("users."+event.getPlayer().getName()).equals("none"))) {
            Tablist.apply(event.getPlayer());
        } else {
            String playerGroup = Pex.getPlayerGroup(event.getPlayer());
            Tablist.apply(event.getPlayer(), playerGroup);
        }}catch(Exception e) {
            e.printStackTrace();
        }
        try {
            DerpUtils.getInstance().aa.addPlayer(event.getPlayer());
            DerpUtils.getInstance().sr.beginTimerForPlayer(event.getPlayer());
        DerpUtils.getInstance().mm.beginTimerForPlayer(event.getPlayer());
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        DerpUtils.getInstance().mm.stopTimerForPlayer(event.getPlayer());
        DerpUtils.getInstance().sr.stopTimerForPlayer(event.getPlayer());
    }
    
    public void sendFloatingText(Player player, String title, int fadeIn, int stay, int fadeOut) {
        new TitleObject(title, TitleObject.TitleType.TITLE).setFadeIn(fadeIn).setFadeOut(fadeOut).setStay(stay).send(player);
    }
    
    public void sendFloatingText(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        new TitleObject(title, subtitle).setFadeIn(fadeIn).setFadeOut(fadeOut).setStay(stay).send(player);
    }
}
