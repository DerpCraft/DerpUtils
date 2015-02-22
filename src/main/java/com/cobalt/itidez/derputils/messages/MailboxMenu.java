/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messages;

import ninja.amp.ampmenus.MenuListener;
import ninja.amp.ampmenus.items.CloseItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import ninja.amp.ampmenus.menus.MailHolder;
import ninja.amp.ampmenus.menus.MenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author iTidez
 */
public class MailboxMenu extends ItemMenu {
    private JavaPlugin plugin;
    
    public MailboxMenu(JavaPlugin plugin) {
        super("Mailbox", Size.TWO_LINE, plugin);
        
        this.plugin = plugin;
        setItem(0, new CloseItem());
    }
    
    /**
     * Opens the {@link ninja.amp.ampmenus.menus.ItemMenu} for a player.
     *
     * @param player The player.
     */
//    @Override
//    public void open(Player player) {
//        if (!MenuListener.getInstance().isRegistered(plugin)) {
//            MenuListener.getInstance().register(plugin);
//        }
//        Inventory inventory = Bukkit.createInventory(new MailHolder(this, Bukkit.createInventory(player, this.getSize().getSize())), this.getSize().getSize(), this.getName());
//        apply(inventory, player);
//        player.openInventory(inventory);
//    }

    /**
     * Updates the {@link ninja.amp.ampmenus.menus.ItemMenu} for a player.
     *
     * @param player The player to update the {@link ninja.amp.ampmenus.menus.ItemMenu} for.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void update(Player player) {
        if (player.getOpenInventory() != null) {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (inventory.getHolder() instanceof MenuHolder && ((MenuHolder) inventory.getHolder()).getMenu().equals(this)) {
                apply(inventory, player);
                player.updateInventory();
            }
        }
    }
}
