/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 *
 * @author iTidez
 */
public class Pex {
    private static PermissionManager pex = PermissionsEx.getPermissionManager();
    
    @SuppressWarnings("deprecation")
    public static String[] getGroups() {
        List<String> groupList = new ArrayList<String>();
        PermissionGroup[] groups = pex.getGroups();
        for(PermissionGroup g : groups) {
            groupList.add(g.getName());
        }
        String[] groupArray = groupList.toArray(new String[groupList.size()]);
        return groupArray;
    }
    
    @SuppressWarnings("deprecation")
    public static String getPlayerGroup(Player player) {
        PermissionUser user = pex.getUser(player);
        String[] playerGroups = user.getGroupsNames();
        String playerGroup = playerGroups[0];
        return playerGroup;
    }
    
    public static String getPlayerPrefix(Player player) {
        PermissionUser user = pex.getUser(player);
        String s = user.getPrefix().replaceAll("&", "§");
        if(s.length() > 16) {
            s = s.substring(0, 15);
        }
        return s;
    }
}
