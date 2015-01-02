/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.achievements;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author iTidez
 */
public abstract class Achievement {
    public static final String MESSAGE = ".MESSAGE";
    public static final String ACHIEVEMENT = ".ACHIEVEMENT";
    public static final String LORE = ".LORE";
    public static final String COLOR = ".COLOR";
    private AchievementManager manager;
    private Material material;
    private String name;
    private String message;
    private ChatColor color;
    private List<String> lore;
    
    public Achievement(String name, Material material) {
        this.name = name;
        this.material = material;
        this.color = ChatColor.YELLOW;
        this.lore = new ArrayList<>();
        this.message = " has just earned the achievement ";
    }
    
    final void registerAchievement(AchievementManager manager) {
        this.manager = manager;
    }
    
    final void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setColor(ChatColor color) {
        this.color = color;
    }
    
    public void setLore(List<String> lore) {
        this.lore = lore;
    }
    
    public ChatColor getColor() {
        return this.color;
    }
    
    public List<String> getLore() {
        return this.lore;
    }
    
    public boolean isRegistered() {
        if(this.manager != null)
            return this.manager.isRegistered(this);
        return false;
    }
    
    public void giveAchievement(OfflinePlayer player) {
        if(isRegistered())
            this.manager.giveAchievement(player, this);
    }
    
    public Material getIcon() {
        return this.material;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getColorName() {
        return new StringBuilder().append(this.color).append(this.name).toString();
    }
    
    public String getMessage(OfflinePlayer player) {
        if(!isRegistered())
            return null;
        StringBuilder builder = new StringBuilder();
        builder.append(new StringBuilder().append(getColorName()).append("\n").toString());
        for(String s : this.lore)
            builder.append(new StringBuilder().append(s).append("\n").toString());
        String string = builder.toString();
        string = string.substring(0, string.length() - 1);
        String message = this.manager.getAchievementJson().replace(".ACHIEVEMENT", getName()).replace(".COLOR", getColor().toString()).replace(".MESSAGE", new StringBuilder().append(player.getName()).append(getMessage()).toString());
        
        return message;
    }
    
    public String toString() {
        return new StringBuilder().append(this.name).append(".yml").toString();
    }
    
}
