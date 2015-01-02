/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.achievements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author iTidez
 */
public class AchievementManager {
    private static List<JavaPlugin> activePlugins = new ArrayList<>();
    private static HashMap<Achievement, JavaPlugin> registeredAchievements = new HashMap<>();
    
    public AchievementManager(JavaPlugin plugin) {
        addActivePlugin(plugin);
    }
    
    public boolean registerAchievement(Achievement achievement, JavaPlugin pl) {
        boolean value = this.registeredAchievements.containsKey(achievement);
        if(value) {
            this.registeredAchievements.put(achievement, pl);
            Bukkit.getPluginManager().registerEvents(achievement, pl);
            achievement.registerAchievement(this);
        }
        return value;
    }
    
    public Collection<Achievement> getRegisteredAchievements(String name) {
        Collection set = new HashSet();
        for(Achievement a : this.registeredAchievements.keySet()) {
            if(a.getName().equals(name))
                set.add(a);
        }
        return Collections.unmodifiableCollection(set);
    }
    
    public boolean isRegistered(Achievement achievement) {
        return this.registeredAchievements.containsKey(achievement);
    }
    
    public void announceAchievement(OfflinePlayer player, Achievement achievement) {
        String json = " " + achievement.getMessage(player);
        for(Player p : Bukkit.getOnlinePlayers())
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + json);
    }
    
    public void announceAchievement(OfflinePlayer player, Achievement achievement, List<Player> recievers) {
        String json = " " + achievement.getMessage(player);
        for(Player p : recievers)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + json);
    }
    
    public String getAchievementJson() {
        return "{text:\".MESSAGE\",extra:[{text:\".COLOR[.ACHIEVEMENT]\",hoverEvent:{action:show_text,value:\".LORE\"}}]}";
    }
    
    private void addActivePlugin(JavaPlugin pl) {
        if(!activePlugins.contains(pl)) {
            activePlugins.add(pl);
        }
    }
    
    public void removePlugin(JavaPlugin pl) {
        if(activePlugins.contains(pl))
            activePlugins.remove(pl);
    }
    
}
