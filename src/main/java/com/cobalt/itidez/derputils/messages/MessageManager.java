/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messages;

import com.cobalt.itidez.derputils.Db;
import com.cobalt.itidez.derputils.DerpUtils;
import com.cobalt.itidez.derputils.commands.Command;
import com.cobalt.itidez.derputils.commands.CommandArgs;
import com.cobalt.itidez.derputils.config.Config;
import com.cobalt.itidez.derputils.config.ConfigManager;
import com.cobalt.itidez.derputils.messageutils.SLAPI;
import io.puharesource.mc.titlemanager.api.ActionbarTitleObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ninja.amp.ampmenus.items.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 *
 * @author iTidez
 */
public class MessageManager {
    private Config messageConfig;
    private HashMap<String, List<String>> loadedPlayers;
    //private List<String> messages;
    private List<Message> loadedMessages;
    private HashMap<String, Runnable> playerTaskTimers;
    private File slapiFile = new File(DerpUtils.getInstance().getDataFolder(), "messages.bin");
    private Db db;
    
    public MessageManager() {
        loadedPlayers = new HashMap<>();
        playerTaskTimers = new HashMap<>();
        loadedMessages = new ArrayList<>();
        DerpUtils.getInstance().log("Logging all loaded messages");
        try {
            //db = new Db(DerpUtils.getInstance(), "localhost", "itidez_messages", "itidez_messages", "plurlife1337");
            loadedMessages = (List<Message>)SLAPI.load(slapiFile);
        } catch (Exception ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        //loadMessages();
        //messageConfig = ConfigManager.createConfig(DerpUtils.getInstance().getDataFolder(), "messages.yml");
        //initMessageConfig();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(DerpUtils.getInstance(), new Runnable() {
            @Override
            public void run() {
                startTimers();
            }
        }, 0l, 40l);
    }
    
    private void startTimers() {
        for(Runnable r : playerTaskTimers.values()) {
            r.run();
        }
    }
    
    public void stopTimerForPlayer(Player p) {
        if(playerTaskTimers.containsKey(p.getName())) {
            playerTaskTimers.remove(p.getName());
        }
    }
    
    private void initMessageConfig() {
        /*if(!messageConfig.hasValue("messages")) {
            messages.add("null|null|null");
            messageConfig.setValue("messages", messages);
        }
        messages = (List<String>)messageConfig.getValue("messages");
        for(String s : messages) {
            loadedMessages.add(new Message(s));
        }*/
        ResultSet rs = db.query("SELECT *");
        
    }
    
    public void beginTimerForPlayer(final Player p) {
        try {
        if(!playerTaskTimers.containsKey(p.getName())) {
            DerpUtils.getInstance().log("Begining timer for "+p.getName());
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if(p != null) {
                        startActionNotifications(p);
                    }
                }
            };
            
            playerTaskTimers.put(p.getName(), r);
        }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        
        ActionManager am = DerpUtils.getInstance().aa;
        ActionbarAnnouncement aa = am.getAnnouncement(p);
    }
    
    public void startActionNotifications(Player p) {
        int i0 = 0;
        ActionManager am = DerpUtils.getInstance().aa;
        for(Message m : loadedMessages) {
            if(m.getTarget().getName().equals(p.getName())) {
                i0++;
            }
        }
        if(i0 == 1) {
            //new ActionbarTitleObject("You have "+ChatColor.RED+"1"+ChatColor.WHITE+" unread message").send(p);
            am.getAnnouncement(p).addComponent("derpm", "You have "+ChatColor.RED+"1"+ChatColor.WHITE+" unread message");
        } else if(i0 > 1) {
            //new ActionbarTitleObject("You have "+ChatColor.RED+i0+ChatColor.WHITE+" unread messages").send(p);
            am.getAnnouncement(p).addComponent("derpm", "You have "+ChatColor.RED+i0+ChatColor.WHITE+" unread messages");
        } else if(i0 == 0) {
            am.getAnnouncement(p).removeComponent("derpm");
        }
    }
    
    /**
     * Begin command reference
     */
    
    @Command(name="mailbox", permission="derp.mailbox", aliases={"mb"})
    public void onMailCommand(CommandArgs args) {
        String[] arguments = args.getArgs();
        //Inventory mailbox = Bukkit.getServer().createInventory(null, 10);
        if(arguments.length < 1) {
            // Start Help Page
            Player p = args.getPlayer();
            p.sendMessage(ChatColor.DARK_GRAY+"/mailbox send <player> <message> - sends a player a letter");
            p.sendMessage(ChatColor.DARK_GRAY+"/mailbox read - Opens your mailbox if you have mail");
            p.sendMessage(ChatColor.DARK_GRAY+"                Click on the message to get the letter");
        } else {
            if(args.getArgs(0).equalsIgnoreCase("send"))
                sendMail(args);
            if(args.getArgs(0).equalsIgnoreCase("read")) {
                readMail(args);
            }
        }
    }
    
    private void sendMail(CommandArgs args) {
        String message = "";
        int i = 0;
        for(String s : args.getArgs()) {
            if(i > 1)
                message += args.getArgs(i)+" ";
            i++;
        }
        Message m = new Message(Bukkit.getPlayer(args.getArgs(1)), Bukkit.getPlayer(args.getPlayer().getName()), message);
        loadedMessages.add(m);
        //messages.add(m.toString());
        //saveMessages();
        //messageConfig.setValue("messages", messages);
        slapiFile.delete();
        try {
            SLAPI.save(loadedMessages, slapiFile);
        } catch (Exception ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        args.getPlayer().sendMessage(ChatColor.GOLD+"["+ChatColor.RED+"me"+ChatColor.GOLD+" -> "+ChatColor.RED+m.getTarget().getName()+ChatColor.GOLD+"] "+ChatColor.WHITE+m.getMessage());
    }
    
    private void readMail(CommandArgs args) {
        int total = 0;
        MailboxMenu mailMenu = new MailboxMenu(DerpUtils.getInstance());
        int i = 1;
        try {
        for(Message m : loadedMessages) {
            if(m.getTarget() == args.getPlayer()) {
                //args.getPlayer().sendMessage(ChatColor.GOLD+"["+ChatColor.RED+m.getSender().getName()+ChatColor.GOLD+" -> "+ChatColor.RED+"me"+ChatColor.GOLD+"] "+ChatColor.WHITE+m.getMessage());
                
                /**
                 * Start new chest GUI
                 */
                ItemStack messageBook = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta bm = (BookMeta)messageBook.getItemMeta();
                bm.setAuthor(m.getSender().getDisplayName());
                bm.addPage(m.getMessage());
                List<String> lore = new ArrayList<>();
                lore.add("DerpPM");
                bm.setLore(lore);
                messageBook.setItemMeta(bm);
                MailItem bookItem = new MailItem(ChatColor.DARK_AQUA+"Unread Message", messageBook, mailMenu, i, args.getPlayer());
                mailMenu.setItem(i, bookItem);
                i++;
                //messages.remove(m.toString());
                loadedMessages.remove(m);
                //saveMessages();
                //messageConfig.setValue("messages", messages);
                slapiFile.delete();
                try {
                    SLAPI.save(loadedMessages, slapiFile);
                } catch (Exception ex) {
                    Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                total++;
            }
        }
        } catch(Exception e) {}
        if(total == 0) {
            args.getPlayer().sendMessage(ChatColor.RED+"You have no messages that need reading");
        } else {
            mailMenu.open(args.getPlayer());
        }
    }
}
