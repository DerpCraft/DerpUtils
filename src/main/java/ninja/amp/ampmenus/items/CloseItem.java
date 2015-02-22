/*
 * This file is part of AmpMenus.
 *
 * Copyright (c) 2014 <http://github.com/ampayne2/AmpMenus/>
 *
 * AmpMenus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AmpMenus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AmpMenus.  If not, see <http://www.gnu.org/licenses/>.
 */
package ninja.amp.ampmenus.items;

import java.util.ArrayList;
import java.util.List;
import ninja.amp.ampmenus.events.ItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A {@link ninja.amp.ampmenus.items.StaticMenuItem} that closes the {@link ninja.amp.ampmenus.menus.ItemMenu}.
 */
public class CloseItem extends StaticMenuItem {

    public CloseItem() {
        super(ChatColor.RED + "Close", new ItemStack(Material.RECORD_4));
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillClose(true);
    }
    
    @Override
    public ItemStack getFinalIcon(Player player) {
        ItemStack finalIcon = super.getFinalIcon(player);
        finalIcon.setType(Material.BARRIER);
        ItemMeta meta = finalIcon.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED+"Close Menu");
        List<String> lore = new ArrayList<>();
        lore.add("Close the mailbox");
        meta.setLore(lore);
        finalIcon.setItemMeta(meta);
        return finalIcon;
    }
}
