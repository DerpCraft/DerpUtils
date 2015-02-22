/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.fakeplayers;

import com.cobalt.itidez.derputils.DerpUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author iTidez
 */
public class FakePlayerManager {
    
    private int currentOnlinePlayers;
    
    public static void sendFakePlayerPackets(String username, boolean online, int ping) {
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        for(Player p : Bukkit.getOnlinePlayers()) {
            PacketContainer fakePacket = pm.createPacket(PacketType.Play.Server.PLAYER_INFO);
            fakePacket.getStrings().write(0, username);
            fakePacket.getBooleans().write(0, Boolean.valueOf(online));
            fakePacket.getIntegers().write(0, ping);
            try {
                pm.sendServerPacket(p, fakePacket);
            } catch (InvocationTargetException ex) {}
        }
    }
    
    public static void startMOTDListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(DerpUtils.getInstance(), new PacketType[] { PacketType.Status.Server.OUT_SERVER_INFO }) {
                    public void onPacketSending(PacketEvent event) {
                        ((WrappedServerPing)event.getPacket().getServerPings().getValues().get(0)).setPlayersOnline(DerpUtils.getInstance().getOnlinePlayers());
                    }
                });
    }
}
