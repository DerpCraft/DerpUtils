/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.achievements;

import com.cobalt.itidez.derputils.DerpUtils;
import com.cobalt.itidez.derputils.commands.Command;
import com.cobalt.itidez.derputils.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author iTidez
 */
public class AchivementManager {
    public AchivementManager() {
        
    }
    
    public void sendNotification(Player p, String title, String subtitle, String url) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+ p.getName() +" reset");
        if(url != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+p.getName()+" subtitle {\"text\":\""+
                    ChatColor.translateAlternateColorCodes('&', subtitle)+"\", \"color\":\"yellow\""
                    + ", \"clickEvent\":{\"action\":\"open_url\", \"value\":\""+url+"\"}}");
            DerpUtils.getInstance().debug("Sending URL");
        }
        else
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+p.getName()+" subtitle {\"text\":\""+
                    ChatColor.translateAlternateColorCodes('&', subtitle)+"\", \"color\":\"yellow\"}");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+p.getName()+" title {\"text\":\""+
                ChatColor.translateAlternateColorCodes('&', title)+"\", \"color\":\"yellow\"}");
    }    
    
    @Command(name="notify", permission="derp.notify")
    public void onNotifyCommand(CommandArgs args) {
        String[] ar = args.getArgs();
        if(ar.length > 1) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title @a reset");
            String reciever = "";
            if(ar[0].equals("all")) {
                reciever = "@a";
            } else {
                OfflinePlayer player = Bukkit.getOfflinePlayer(ar[0]);
                reciever = player.getName();
            }
            StringBuilder lineBuilder = new StringBuilder();
            int i = 1;
            String current = ar[i];
            while(!current.equals(":")) {
                lineBuilder.append(current).append(" ");
                i++;
                try {
                    if(ar[i] == null)
                        break;
                    else
                        current = ar[i];
                } catch(Exception ex) {
                    break;
                }
            }
            i++;
            StringBuilder sublineBuilder = new StringBuilder();
            if(ar[i] != null) {
                current = ar[i];
                try {
                while(current != null) {
                    sublineBuilder.append(current).append(" ");
                    i++;
                    current = ar[i];
                }
                } catch(Exception e){}
            }
            
            if(!sublineBuilder.toString().isEmpty())
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+reciever+" subtitle {\"text\":\""+
                        ChatColor.translateAlternateColorCodes('&', sublineBuilder.toString())+"\", \"color\":\"dark_aqua\"}");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+reciever+" title {\"text\":\""+
                    ChatColor.translateAlternateColorCodes('&', lineBuilder.toString())+"\", \"color\":\"dark_aqua\"}");
        }
    }
}
