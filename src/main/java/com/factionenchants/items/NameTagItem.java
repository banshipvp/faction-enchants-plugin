package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Factory for the custom Name Tag item used to rename any held item
 * with full colour-code support via a chat-based confirmation flow.
 */
public class NameTagItem {

    private static final String PDC_KEY = "custom_name_tag";

    /** Create a Name Tag ItemStack. */
    public static ItemStack create(FactionEnchantsPlugin plugin) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6§lName Tag");
        meta.setLore(List.of(
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Right-click to rename any",
                "§7item with custom colours!",
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§eRight-click to use"
        ));
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    /** Returns true if the given item is a custom Name Tag. */
    public static boolean isNameTag(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE);
    }
}
