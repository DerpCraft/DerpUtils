/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.fakeplayers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author iTidez
 */
public class FakePlayer {
    private String name;
    private Location loc;
    private int ping;
    
    public FakePlayer(String username) {
        this(username, Bukkit.getWorld("world").getSpawnLocation(), 9999);
    }
    
    public FakePlayer(String username, Location loc) {
        this(username, loc, 9999);
    }
    
    public FakePlayer(String username, Location loc, int ping) {
        this.name = username;
        this.loc = loc;
        this.ping = ping;
        init();
    }
    
    private void init() {
        FakePlayerManager.sendFakePlayerPackets(name, true, ping);
    }
}
