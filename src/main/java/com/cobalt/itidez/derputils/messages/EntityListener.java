/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messages;

import com.cobalt.itidez.derputils.DerpUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.BookMeta;

/**
 *
 * @author iTidez
 */
public class EntityListener implements Listener {
    public EntityListener() {
    }
    
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            
            //DerpUtils.getInstance().log("Found that the player right clicked something");
            if(p.getItemInHand().getType() == Material.WRITTEN_BOOK) {
                BookMeta bm = (BookMeta)p.getItemInHand().getItemMeta();
                //DerpUtils.getInstance().log("We found that the item was a written book named: "+bm.getTitle());
                if(bm.getLore().contains("DerpPM")) {
                    Bukkit.getScheduler().scheduleAsyncDelayedTask(DerpUtils.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                             p.getInventory().remove(p.getItemInHand());
                        }
                    }, 40l);
                }
            }
        }
    }
}
