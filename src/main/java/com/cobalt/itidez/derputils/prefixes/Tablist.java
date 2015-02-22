/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.prefixes;

import com.cobalt.itidez.derputils.DerpUtils;
import com.cobalt.itidez.derputils.commands.Command;
import com.cobalt.itidez.derputils.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author iTidez
 */
public class Tablist {
    public static void apply(Player player, String playerGroup) {
        String longDisplayName = ChatColor.valueOf((String)DerpUtils.getInstance().getUtilsConfig().getValue("groups."+playerGroup)) + player.getDisplayName();
        String displayName = longDisplayName;
        if(longDisplayName.length() > 16) {
            displayName = longDisplayName.substring(0, 16);
        }
        player.setPlayerListName(displayName);
    }
    
    public static void apply(Player player) {
        String longDisplayName = ChatColor.translateAlternateColorCodes('&',(String)DerpUtils.getInstance().getUtilsConfig().getValue("users."+player.getName()));
        String displayName = longDisplayName;
        if(longDisplayName.length() > 16) {
            displayName = longDisplayName.substring(0, 16);
        }
        player.setPlayerListName(displayName);
    }
    
    @Command(name="tabname", permission="derp.tabname")
    public void onTabNameCommand(CommandArgs args) {
        if(args.getArgs().length < 1) {
            args.getPlayer().sendMessage(ChatColor.RED+"Error: You need to type in a username and tabname");
            return;
        } else if(args.getArgs().length < 2) {
            args.getPlayer().sendMessage(ChatColor.RED+"Error: You need to type in a tabname");
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(args.getArgs(0));
        targetPlayer.setPlayerListName(ChatColor.translateAlternateColorCodes('&', args.getArgs(1)));
        DerpUtils.getInstance().getUtilsConfig().setValue("users."+targetPlayer.getName(), args.getArgs(1));
    }
}
