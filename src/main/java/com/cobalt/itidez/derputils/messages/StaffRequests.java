/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messages;

import com.cobalt.itidez.derputils.DerpUtils;
import com.cobalt.itidez.derputils.commands.Command;
import com.cobalt.itidez.derputils.commands.CommandArgs;
import com.cobalt.itidez.derputils.messageutils.SLAPI;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 *
 * @author iTidez
 */
public class StaffRequests {
    private List<Request> loadedMessages;
    private HashMap<UUID, Runnable> playerTaskTimers;
    private File slapiFile = new File(DerpUtils.getInstance().getDataFolder(), "requests.bin");
    private List<UUID> playersWithTickets;
    private static final String DB_NAME = "jdbc:mysql://localhost:3306/staffrequests";
    private static final String USER = "staffrequests";
    private static final String PASS= "CobaltSys1337";
    private Connection con;
    private Statement s;
    
    public StaffRequests() {
        playerTaskTimers = new HashMap<>();
        loadedMessages = new ArrayList<>();
        playersWithTickets = new ArrayList<>();
        DerpUtils.getInstance().log("Logging all loaded messages");
        /*try {
            loadedMessages = (List<Request>)SLAPI.load(slapiFile);
        } catch (Exception ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        try {
            Class.forName("com.mysql.jdbc.Driver"); //Gets the driver class
 
        con = DriverManager.getConnection(DB_NAME, USER, PASS); //Gets a connection to the database using the details you provided.
 
        String sql = "SELECT * FROM requests";
        s = con.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM requests");
        rs.first();
        do {
            Request r = new Request(rs.getInt("id"), Bukkit.getPlayer(UUID.fromString(rs.getString("originUUID"))), rs.getString("location"), rs.getString("request"));
            loadedMessages.add(r);
            
        } while(rs.next());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        try {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(DerpUtils.getInstance(), new Runnable() {
            @Override
            public void run() {
                startTimers();
            }
        }, 0l, 40l);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private Location getLocationFromString(String s) {
        String[] splitLoc = s.split("|");
        return new Location(Bukkit.getWorld("world"), Integer.getInteger(splitLoc[0]), Integer.getInteger(splitLoc[1]), Integer.getInteger(splitLoc[2]));
    }
    
    private void startTimers() {
        for(Runnable r : playerTaskTimers.values()) {
            r.run();
        }
    }
    
    public void stopTimerForPlayer(Player p) {
        if(playerTaskTimers.containsKey(p.getUniqueId())) {
            playerTaskTimers.remove(p.getUniqueId());
        }
    }
    
    public void beginTimerForPlayer(final Player p) {
        try {
        if(!playerTaskTimers.containsKey(p.getUniqueId())) {
            DerpUtils.getInstance().log("Begining timer for "+p.getName());
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if(p != null) {
                        startActionNotifications(p);
                    }
                }
            };
            
            playerTaskTimers.put(p.getUniqueId(), r);
        }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        
        ActionManager am = DerpUtils.getInstance().aa;
        ActionbarAnnouncement aa = am.getAnnouncement(p);
    }
    
    public void startActionNotifications(Player p) {
        int i0 = 0;
        try {
        ActionManager am = DerpUtils.getInstance().aa;
        for(Request m : loadedMessages) {
            if(p.hasPermission("derp.staff")) {
                i0++;
            }
        }
        if(i0 == 1) {
            //new ActionbarTitleObject("You have "+ChatColor.RED+"1"+ChatColor.WHITE+" unread message").send(p);
            am.getAnnouncement(p).addComponent("derstaff", "There is "+ChatColor.RED+"1"+ChatColor.WHITE+" current staff request");
        } else if(i0 > 1) {
            //new ActionbarTitleObject("You have "+ChatColor.RED+i0+ChatColor.WHITE+" unread messages").send(p);
            am.getAnnouncement(p).addComponent("derstaff", "There are "+ChatColor.RED+i0+ChatColor.WHITE+" current staff requests");
        } else if(i0 == 0) {
            am.getAnnouncement(p).removeComponent("derstaff");
        }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @Command(name="req", permission="derp.user", aliases={"request"})
    public void onRequestCommand(CommandArgs args) {
        if(playersWithTickets.contains(args.getPlayer().getUniqueId())) {
            args.getPlayer().sendMessage(ChatColor.RED+"Error: You already have a ticket. Please use '/req send' to send your ticket");
        } else {
            ItemStack is = new ItemStack(Material.BOOK_AND_QUILL);
            args.getPlayer().getInventory().addItem(is);
            args.getPlayer().sendMessage(ChatColor.GRAY+"Please edit the book with your desired message and type /req send to send your request!");
            playersWithTickets.add(args.getPlayer().getUniqueId());
        }
    }
    
    @Command(name="req.send", permission="derp.user", aliases={"request.send"})
    public void onRequestSendCommand(CommandArgs args) {
        try {
        if(playersWithTickets.contains(args.getPlayer().getUniqueId())) {
            ItemStack is = args.getPlayer().getItemInHand();
            if(is.getType() == Material.WRITTEN_BOOK) {
                BookMeta bm = (BookMeta)is.getItemMeta();
                List<String> ticketPages = bm.getPages();
                String compiledRequest = "";
                for(String s : ticketPages) {
                    compiledRequest += s+" ";
                }
                String sql = "INSERT INTO requests (originUUID, location, request) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, args.getPlayer().getUniqueId().toString());
                ps.setString(2, args.getPlayer().getLocation().getBlockX()+"|"+args.getPlayer().getLocation().getBlockY()+"|"+args.getPlayer().getLocation().getBlockZ());
                ps.setString(3, compiledRequest);
                ps.executeUpdate();
                ps.close();
                sql = "SELECT id FROM requests WHERE request='"+compiledRequest+"'";
                s = con.createStatement();
                ResultSet rs = s.executeQuery(sql);
                rs.first();
                Request req = new Request(rs.getInt("id"), args.getPlayer(), args.getPlayer().getLocation().getBlockX()+"|"+args.getPlayer().getLocation().getBlockY()+"|"+args.getPlayer().getLocation().getBlockZ(), compiledRequest);
                rs.close();
                loadedMessages.add(req);
                playersWithTickets.remove(args.getPlayer().getUniqueId());
                args.getPlayer().sendMessage(ChatColor.GREEN+"Your request has been recieved and is displaying to all staff members!");
                args.getPlayer().getInventory().remove(args.getPlayer().getItemInHand());
            }
        }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @Command(name="req.read", permission="derp.staff")
    public void onStaffRead(CommandArgs args) {
        for(Request r : loadedMessages) {
            ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bm = (BookMeta)is.getItemMeta();
            bm.addPage(r.getOriginPlayer().getDisplayName()+"\n\n\n"+r.getRequestMessage()+"\n"+r.getLocationString()+"\n\n\nTicket ID: "+r.getID());
            bm.setAuthor(r.getOriginPlayer().getDisplayName());
            is.setItemMeta(bm);
            args.getPlayer().getInventory().addItem(is);
        }
    }
    @Command(name="req.close", permission="derp.staff")
    public void onStaffComplete(CommandArgs args) {
        String[] arg = args.getArgs();
        if(arg.length < 1) {
            
        } else {
            for(Request r : loadedMessages) {
                if(arg[0].equalsIgnoreCase((""+r.getID()))) {
                    loadedMessages.remove(r);
                    try {
                        String sql = "DELETE FROM requests WHERE id='"+r.getID()+"'";
                        s = con.createStatement();
                        s.executeUpdate(sql);
                        args.getPlayer().sendMessage(ChatColor.GREEN+"Closed Ticket");
                    } catch (SQLException ex) {
                        Logger.getLogger(StaffRequests.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
