/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messages;

import com.cobalt.itidez.derputils.DerpUtils;
import com.cobalt.itidez.derputils.commands.Command;
import com.cobalt.itidez.derputils.commands.CommandArgs;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author iTidez
 */
public class Request {
    private int uuid;
    private String originUUID;
    private String requestMessage;
    private String location;
    
    public Request(int id ,Player p, String location, String requestMessage) {
        uuid = id;
        originUUID = p.getUniqueId().toString();
        this.requestMessage = requestMessage;
        this.location = location;
    }
    
    /*public Request(String id, Player p, String location, String requestMessage) {
        uuid = id;
        originUUID = p.getUniqueId().toString();
        this.requestMessage = requestMessage;
        this.location = location;
    }*/
    
    public int getID() {
        return uuid;
    }
    
    public Player getOriginPlayer() {
        return Bukkit.getPlayer(UUID.fromString(originUUID));
    }
    
    public String getRequestMessage() {
        return requestMessage;
    }
    
    public Location getOriginLocation() {
        return getLocationFromString(location);
    }
    
    private Location getLocationFromString(String s) {
        String[] splitLoc = location.split("|");
        return new Location(Bukkit.getWorld("world"), Integer.getInteger(splitLoc[0]), Integer.getInteger(splitLoc[1]), Integer.getInteger(splitLoc[2]));
    }
    
    public String getLocationString() {
        return location;
    }
    
    public String toString() {
        return uuid+"?"+originUUID+"?"+location+"?"+requestMessage;
    }
    
}
