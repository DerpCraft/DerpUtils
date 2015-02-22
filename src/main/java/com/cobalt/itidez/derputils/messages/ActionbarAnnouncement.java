/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author iTidez
 */
public class ActionbarAnnouncement {
    private UUID playerUUID;
    private HashMap<String, String> messageComponents;
    
    public ActionbarAnnouncement(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.messageComponents = new HashMap<>();
    }
    
    public ActionbarAnnouncement(Player player) {
        this.playerUUID = player.getUniqueId();
        this.messageComponents = new HashMap<>();
    }
    
    public ActionbarAnnouncement addComponent(String id, String cmp) {
        if(messageComponents.containsKey(id)) {
            messageComponents.remove(id);
            messageComponents.put(id, cmp);
        } else {
            messageComponents.put(id, cmp);
        }
        return this;
    }
    
    public ActionbarAnnouncement removeComponent(String id) {
        if(messageComponents.containsKey(id)) {
            messageComponents.remove(id);
        }
        return this;
    }
    
    public boolean has(String id) {
        return messageComponents.containsKey(id);
    }
    
    public List<String> getComponents() {
        List<String> lists = new ArrayList<>();
        for(String s : messageComponents.values()) {
            lists.add(s);
        }
        return lists;
    }
    
    public String getComponent(String id) {
        if(messageComponents.containsKey(id))
            return messageComponents.get(id);
        else
            return null;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }
}
