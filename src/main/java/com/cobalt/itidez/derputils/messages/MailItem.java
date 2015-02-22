/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messages;

import java.util.ArrayList;
import java.util.List;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author iTidez
 */
public class MailItem extends MenuItem {
    private ItemStack is;
    private ItemMenu im;
    private int pos;
    private Player p;
    public MailItem(String title, ItemStack is, ItemMenu im, int pos, Player p) {
        super(ChatColor.DARK_AQUA + title, is);
        this.is = is;
        this.im = im;
        this.pos = pos;
        this.p = p;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.getPlayer().getInventory().addItem(is);
        this.im.setItem(pos, new MenuItem("Read Message" ,new ItemStack(Material.AIR)));
        this.im.update(p);
    }
}
