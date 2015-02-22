/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.prefixes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author iTidez
 */
public class Teams {
    public static void addColor(Player player, String prefix) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        
        Team team = scoreboardManager.getMainScoreboard().getTeam(player.getName());
        
        if(team == null) {
            Team t = scoreboardManager.getMainScoreboard().registerNewTeam(player.getName());
            t.setPrefix(prefix);
        }
    }
    
    public static void shutdown() {
        for(Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            team.unregister();
        }
    }
}
