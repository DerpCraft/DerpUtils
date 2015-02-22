/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messages;

import com.cobalt.itidez.derputils.DerpUtils;
import java.io.Serializable;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author iTidez
 */
public class Message implements Serializable {
    
    private String target;
    private String sender;
    private String message;
    
    public Message(Player target, Player sender, String message) {
        this.target = target.getUniqueId().toString();
        this.sender = sender.getUniqueId().toString();
        this.message = message;
    }
    
    public Message(String s) {
        String[] splitPlayers = s.split("|");
        this.target = splitPlayers[0];
        this.sender = splitPlayers[1];
        this.message = splitPlayers[2];
    }
    
    public Player getTarget() {
        UUID targetUUID = UUID.fromString(target);
        return Bukkit.getPlayer(targetUUID);
    }
    
    public Player getSender() {
        UUID senderUUID = UUID.fromString(sender);
        return Bukkit.getPlayer(senderUUID);
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return target+"|"+sender+"|"+message;
    }
}
