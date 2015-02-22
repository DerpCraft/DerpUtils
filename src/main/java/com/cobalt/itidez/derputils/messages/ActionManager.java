/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messages;

import com.cobalt.itidez.derputils.DerpUtils;
import io.puharesource.mc.titlemanager.api.ActionbarTitleObject;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author iTidez
 */
public class ActionManager {
    private HashMap<UUID, ActionbarAnnouncement> announcements;
    
    public ActionManager() {
        this.announcements = new HashMap<>();
        init();
    }
    
    public void init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(DerpUtils.getInstance(), new Runnable() {
            @Override
            public void run() {
                for(ActionbarAnnouncement aa : getAnnouncements().values()) {
                    Player t = aa.getPlayer();
                    String compiledMessage = "";
                    for(String s : aa.getComponents()) {
                        compiledMessage += s + " | ";
                    }
                    if(compiledMessage.length() >= 3)
                        compiledMessage = compiledMessage.substring(0, compiledMessage.length() - 3);
                    new ActionbarTitleObject(compiledMessage).send(t);
                }
            }
        }, 0, 40l);
    }
    
    private HashMap<UUID, ActionbarAnnouncement> getAnnouncements() {
        return announcements;
    }
    
    public ActionbarAnnouncement addPlayer(Player p) {
        ActionbarAnnouncement aa = new ActionbarAnnouncement(p);
        announcements.put(p.getUniqueId(), aa);
        return aa;
    }
    
    public void removePlayer(Player p) {
        if(announcements.containsKey(p.getUniqueId()))
            announcements.remove(p.getUniqueId());
    }
    
    public ActionbarAnnouncement getAnnouncement(Player p) {
        return (announcements.containsKey(p.getUniqueId())) ? announcements.get(p.getUniqueId()) : addPlayer(p);
    }
}
