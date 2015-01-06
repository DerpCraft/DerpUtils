package com.cobalt.itidez.derputils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class CooldownManager implements Listener{
	
	/**
	 * (			==========================================			)
	 * (				Copyright by Dealyise @ Bukkit.org				)
	 * (			==========================================			)
	 */
	
	private static List<String> commands = new ArrayList<String>();
	private static Plugin PLUGIN;
	private static Boolean COMMANDCOOLDOWN = false;
	private static Boolean PERMCACHE = false;
	private static Integer STANDARDVALUETIME = 0;
	private static String STANDARDTIMETYPE = null;
	
	/**
	 * (			================			)
	 * (				Settings				)
	 * (			================			)
	 */
	
	public CooldownManager(Plugin plugin){
		this.PLUGIN = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, PLUGIN);
	}
	
	public CooldownManager(Plugin plugin, Boolean activateIntelligentCommandCooldown, Boolean togglePermCache, Integer cooldownValue, String cooldownTimeType){
		this.PLUGIN = plugin;
		this.COMMANDCOOLDOWN = activateIntelligentCommandCooldown;
		this.STANDARDVALUETIME = cooldownValue;
		this.STANDARDTIMETYPE = cooldownTimeType;
		this.PERMCACHE = togglePermCache;
		plugin.getServer().getPluginManager().registerEvents(this, PLUGIN);
	}
	
	public void addCommand(String... commands){
		this.commands.addAll(Arrays.asList(commands));
	}
	/**
	 * (			==============			)
	 * (			CooldownMethod			)
	 * (			==============			)
	 */
	
	public static Boolean hasCooldown(Player p, String command){
		Long cool = getData(p, command);
		Long curUnix = new Date().getTime()/1000;
		if(cool > curUnix){
			return true;
		}else{
			return false;
		}
	}
	
	public static Boolean hasCooldown(Player p){
		Long cool = getData(p);
		Long curUnix = new Date().getTime()/1000;
		if(cool > curUnix){
			return true;
		}else{
			return false;
		}
	}
	
	public static String returnCooldown(Player player, String command){
		
	
		Long currentUnix = new Date().getTime()/1000;
		Long cooldown = getData(player, command);
		
		String cdReturn = null;
		if(cooldown > currentUnix){
			Double TimeLeft = (double) (cooldown - currentUnix);
			String TimeOutputSeconds = ""; 
			String TimeOutputMinutes = "";
			String TimeOutputHours = "";
			String TimeOutputDays = "";
			if (TimeLeft > 59) {
				if (TimeLeft > 3599) {
					if(TimeLeft > 86399){
						Integer TimeDays = 0;
						TimeDays = (int) (TimeLeft/86400);
						Double TimeHours = 0.000;
						TimeHours = (double) (TimeLeft/3600);
						Double TimeMinutes = 0.000;
						TimeMinutes = (TimeHours - ((int) (TimeLeft/3600)))*60;
						Double TimeSeconds = 0.000;
						TimeSeconds = (TimeMinutes - ((int) ((double)TimeMinutes)))*60;
						
						if (TimeDays == 1) { TimeOutputDays = "Day"; } else { TimeOutputDays = "Days"; }
						if (TimeMinutes == 1) { TimeOutputMinutes = "Minute"; } else if (TimeMinutes == 0 || TimeMinutes > 1) { TimeOutputMinutes = "Minutes"; }
						if (TimeSeconds == 1) { TimeOutputSeconds = "Second"; } else if (TimeSeconds == 0 || TimeSeconds > 1) { TimeOutputSeconds = "Seconds"; }
						
						

						cdReturn = (int) TimeDays + " " + TimeOutputDays + ", " + TimeHours + " " + TimeOutputHours + ", " + ((int) ((double)TimeMinutes)) + " " + TimeOutputMinutes + ", " + (int) Math.round(TimeSeconds) + " " + TimeOutputSeconds;
					}else{
						Double TimeHours = 0.000;
						TimeHours = (double) (TimeLeft/3600);
						Double TimeMinutes = 0.000;
						TimeMinutes = (TimeHours - ((int) (TimeLeft/3600)))*60;
						Double TimeSeconds = 0.000;
						TimeSeconds = (TimeMinutes - ((int) ((double)TimeMinutes)))*60;
					
						if (TimeHours == 1) { TimeOutputHours = "Hour"; } else { TimeOutputHours = "Hours"; }
						if (TimeMinutes == 1) { TimeOutputMinutes = "Minute"; } else if (TimeMinutes == 0 || TimeMinutes > 1) { TimeOutputMinutes = "Minutes"; }
						if (TimeSeconds == 1) { TimeOutputSeconds = "Second"; } else if (TimeSeconds == 0 || TimeSeconds > 1) { TimeOutputSeconds = "Seconds"; }
					
					

						cdReturn = ((int) ((double)TimeHours)) + " " + TimeOutputHours + ", " + ((int) ((double)TimeMinutes)) + " " + TimeOutputMinutes + ", " + (int) Math.round(TimeSeconds) + " " + TimeOutputSeconds;
					}
				} else {
					Double TimeMinutes = 0.000;
					TimeMinutes = (double) (TimeLeft/60);
					Double TimeSeconds = 0.000;
					TimeSeconds = (TimeMinutes - ((int) (TimeLeft/60)))*60;
					
					if (TimeMinutes == 1) { TimeOutputMinutes = "Minute"; } else { TimeOutputMinutes = "Minutes"; }
					if (TimeSeconds == 1) { TimeOutputSeconds = "Second"; } else if (TimeSeconds == 0) { TimeOutputSeconds = "Seconds"; }
					else { TimeOutputSeconds = "Seconds"; }
					
					cdReturn = ((int) ((double)TimeMinutes)) + " " + TimeOutputMinutes + ", " + (int) Math.round(TimeSeconds) + " " + TimeOutputSeconds;
				}
			} else {
				if (TimeLeft == 1) { TimeOutputSeconds = "Second"; } else { TimeOutputSeconds = "Seconds"; }
				
				cdReturn = ((int) ((double)TimeLeft)) + " " + TimeOutputSeconds;
			}
			
		}
		
		return cdReturn;
		
	}
	
	public static String returnCooldown(Player player){
		
		
		Long currentUnix = new Date().getTime()/1000;
		Long cooldown = getData(player);
		
		String cdReturn = null;
		if(cooldown > currentUnix){
			Double TimeLeft = (double) (cooldown - currentUnix);
			String TimeOutputSeconds = ""; 
			String TimeOutputMinutes = "";
			String TimeOutputHours = "";
			String TimeOutputDays = "";
			if (TimeLeft > 59) {
				if (TimeLeft > 3599) {
					if(TimeLeft > 86399){
						Integer TimeDays = 0;
						TimeDays = (int) (TimeLeft/86400);
						Double TimeHours = 0.000;
						TimeHours = (double) (TimeLeft/3600);
						Double TimeMinutes = 0.000;
						TimeMinutes = (TimeHours - ((int) (TimeLeft/3600)))*60;
						Double TimeSeconds = 0.000;
						TimeSeconds = (TimeMinutes - ((int) ((double)TimeMinutes)))*60;
						
						if (TimeDays == 1) { TimeOutputDays = "Day"; } else { TimeOutputDays = "Days"; }
						if (TimeMinutes == 1) { TimeOutputMinutes = "Minute"; } else if (TimeMinutes == 0 || TimeMinutes > 1) { TimeOutputMinutes = "Minutes"; }
						if (TimeSeconds == 1) { TimeOutputSeconds = "Second"; } else if (TimeSeconds == 0 || TimeSeconds > 1) { TimeOutputSeconds = "Seconds"; }
						
						

						cdReturn = (int) TimeDays + " " + TimeOutputDays + ", " + TimeHours + " " + TimeOutputHours + ", " + ((int) ((double)TimeMinutes)) + " " + TimeOutputMinutes + ", " + (int) Math.round(TimeSeconds) + " " + TimeOutputSeconds;
					}else{
						Double TimeHours = 0.000;
						TimeHours = (double) (TimeLeft/3600);
						Double TimeMinutes = 0.000;
						TimeMinutes = (TimeHours - ((int) (TimeLeft/3600)))*60;
						Double TimeSeconds = 0.000;
						TimeSeconds = (TimeMinutes - ((int) ((double)TimeMinutes)))*60;
					
						if (TimeHours == 1) { TimeOutputHours = "Hour"; } else { TimeOutputHours = "Hours"; }
						if (TimeMinutes == 1) { TimeOutputMinutes = "Minute"; } else if (TimeMinutes == 0 || TimeMinutes > 1) { TimeOutputMinutes = "Minutes"; }
						if (TimeSeconds == 1) { TimeOutputSeconds = "Second"; } else if (TimeSeconds == 0 || TimeSeconds > 1) { TimeOutputSeconds = "Seconds"; }
					
					

						cdReturn = ((int) ((double)TimeHours)) + " " + TimeOutputHours + ", " + ((int) ((double)TimeMinutes)) + " " + TimeOutputMinutes + ", " + (int) Math.round(TimeSeconds) + " " + TimeOutputSeconds;
					}
				} else {
					Double TimeMinutes = 0.000;
					TimeMinutes = (double) (TimeLeft/60);
					Double TimeSeconds = 0.000;
					TimeSeconds = (TimeMinutes - ((int) (TimeLeft/60)))*60;
					
					if (TimeMinutes == 1) { TimeOutputMinutes = "Minute"; } else { TimeOutputMinutes = "Minutes"; }
					if (TimeSeconds == 1) { TimeOutputSeconds = "Second"; } else if (TimeSeconds == 0) { TimeOutputSeconds = "Seconds"; }
					else { TimeOutputSeconds = "Seconds"; }
					
					cdReturn = ((int) ((double)TimeMinutes)) + " " + TimeOutputMinutes + ", " + (int) Math.round(TimeSeconds) + " " + TimeOutputSeconds;
				}
			} else {
				if (TimeLeft == 1) { TimeOutputSeconds = "Second"; } else { TimeOutputSeconds = "Seconds"; }
				
				cdReturn = ((int) ((double)TimeLeft)) + " " + TimeOutputSeconds;
			}
			
		}
		
		return cdReturn;
		
	}
	 
	public static void activateCooldown(Player player, String command, String time, Integer cooldown){

		Long currentUnix = new Date().getTime()/1000;
		String[] TimeDiff;
		String MuteTimeOutput = "";
		Long MutedUntil = (long) 0;
		try {
			if (time=="s") {
				if (cooldown == 1) {
					MuteTimeOutput = "Second";
				} else {
					MuteTimeOutput = "Seconds";
				}
				MutedUntil = currentUnix + cooldown;
			} else if (time=="m") {
				if (cooldown == 1) {
					MuteTimeOutput = "Minute";
				} else {
					MuteTimeOutput = "Minutes";
				}
				MutedUntil = currentUnix + (60*cooldown);
			} else if (time=="h") {
				if (cooldown == 1) {
					MuteTimeOutput = "Hour";
				} else {
					MuteTimeOutput = "Hours";
				}
				MutedUntil = currentUnix + (60*60*cooldown);
			} else if (time=="d") {
				if (cooldown == 1) {
					MuteTimeOutput = "Day";
				} else {
					MuteTimeOutput = "Days";
				}
				MutedUntil = currentUnix + (60*60*24*cooldown);
			} 
			
			setPlayerData(command, player, MutedUntil);
			
		}catch(NumberFormatException e){
			java.lang.System.err.println("Please use s,m,h,d as Time!" + e.getMessage());
		}
	}
	
	public static void activateCooldown(Player player, String time, Integer cooldown){

		Long currentUnix = new Date().getTime()/1000;
		String[] TimeDiff;
		String MuteTimeOutput = "";
		Long MutedUntil = (long) 0;
		try {
			if (time=="s") {
				if (cooldown == 1) {
					MuteTimeOutput = "Second";
				} else {
					MuteTimeOutput = "Seconds";
				}
				MutedUntil = currentUnix + cooldown;
			} else if (time=="m") {
				if (cooldown == 1) {
					MuteTimeOutput = "Minute";
				} else {
					MuteTimeOutput = "Minutes";
				}
				MutedUntil = currentUnix + (60*cooldown);
			} else if (time=="h") {
				if (cooldown == 1) {
					MuteTimeOutput = "Hour";
				} else {
					MuteTimeOutput = "Hours";
				}
				MutedUntil = currentUnix + (60*60*cooldown);
			} else if (time=="d") {
				if (cooldown == 1) {
					MuteTimeOutput = "Day";
				} else {
					MuteTimeOutput = "Days";
				}
				MutedUntil = currentUnix + (60*60*24*cooldown);
			} 
			
			setPlayerData(player, MutedUntil);

			
		}catch(NumberFormatException e){
			java.lang.System.err.println("Please use s,m,h,d as Time!" + e.getMessage());
		}
	}
	
	/**
	 * (			============			)
	 * (			DataModifier			)
	 * (			============			)
	 */
	
	private static void cachePlayer(Player p){
		File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("Cooldown.normalCooldown", 0);
		if(commands.size() >= 1){
			for(int i = 0; i < commands.size(); i++){
				cfg.set("Cooldown." + commands.get(i), 0);
			}
		}
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void removePlayerCache(Player p){
		File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
		if(file.exists()){
			file.delete();
		}else{
			System.out.println("[Cooldown] Player Cache '" + p.getName() + "' not found. Abort.");
			return;
		}
	}
	
	private static Boolean isOnCooldown(Player p){
		File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Long curUnix = new Date().getTime()/1000;
		Long pUnix = cfg.getLong("Cooldown.normalCooldown");
		if(pUnix > curUnix){
			return true;
		}else{
			return false;
		}
	}
	
	private static Boolean isPlayerCache(Player p){
		File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
		return file.exists();
	}
	
	private static Boolean isOnCooldown(Player p, String command){
		File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Long curUnix = new Date().getTime()/1000;
		Long pUnix = cfg.getLong("Cooldown." + command);
		if(pUnix > curUnix){
			return true;
		}else{
			return false;
		}
	}
	
	private static void setPlayerData(String cmd, Player p, Long value){
		File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("Cooldown." + cmd, value);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void setPlayerData(Player p, Long value){
		File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("Cooldown.normalCooldown", value);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Long getData(Player p, String command){
		File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getLong("Cooldown." + command);
	}
	
	private static Long getData(Player p){
		File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getLong("Cooldown.normalCooldown");
	}
	
	@EventHandler(priority=EventPriority.LOW)
	private void onPlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(isPlayerCache(p)){
			System.out.println("Player '" + p.getName() + "' is cached already. Abort.");
			return;
		}else{
			cachePlayer(p);
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	private void onPlayerLeave(PlayerQuitEvent e){
		if(PERMCACHE){
			Player p = e.getPlayer();
			if(isOnCooldown(p)){
				System.out.println("Player '" + p.getName() + "' still on Normal Cooldown. Abort.");
				return;
			}else{
				for(int i = 0; i < commands.size(); i++){
					String cmd = commands.get(i);
					if(isOnCooldown(p,cmd)){
						System.out.println("Player '" + p.getName() + "' still on Command Cooldown. Abort.");
						return;
					}else{
						removePlayerCache(p);
						return;
					}
				}
			}
		}else{
			Player p = e.getPlayer();
			removePlayerCache(p);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	private void onPlayerChat(PlayerCommandPreprocessEvent e){
		if(COMMANDCOOLDOWN){
			Player p = e.getPlayer();
			String cmd = e.getMessage();
			
			for(int i = 0; i < commands.size(); i++){
				String cmdT = commands.get(i);
					if(cmd.contains(cmdT)){
						if(hasCooldown(p, cmdT)){
							e.setCancelled(true);
							String cd = returnCooldown(p, cmdT);
							p.sendMessage("�cYou still have a cooldown! Time left: " + cd);
						}else{
							activateCooldown(p, cmdT, STANDARDTIMETYPE, STANDARDVALUETIME);
						}
					}
				}
			
		}else{
			return;
		}
	}
		
	
}
