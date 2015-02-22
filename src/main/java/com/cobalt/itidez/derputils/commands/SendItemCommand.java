/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author iTidez
 */
public class SendItemCommand {
    @Command(name="senditem", aliases={"si"})
    public void onSendItem(CommandArgs args) {
        if(args.getArgs().length < 1) {
            args.getPlayer().sendMessage(ChatColor.RED+"You must specify a player to send an item to");
        }
        if(args.getPlayer().getItemInHand().getType() != Material.AIR) {
            ItemStack is = args.getPlayer().getItemInHand();
            Player target = Bukkit.getPlayer(args.getArgs(0));
            if(target.isOnline()) {
                args.getPlayer().getInventory().remove(is);
                target.getInventory().addItem(is);
                args.getPlayer().sendMessage(ChatColor.GREEN+"You have sent "+args.getArgs(0)+" an item!");
                target.sendMessage(ChatColor.GREEN+"You have been sent an item from "+args.getPlayer().getName());
            } else {
                args.getPlayer().sendMessage(ChatColor.RED+"Please wait for the player to be online before you send them an item");
            }
        } else {
            args.getPlayer().sendMessage(ChatColor.RED+"You must have the item you want to send in your hand");
        }
    }
}
