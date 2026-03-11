package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Factory for Fallen Hero Bone items.
 *
 * Each bone is tied to a specific GKit by name (stored in PDC).
 * Right-clicking the bone summons a "Fallen Hero" mob wearing full Netherite armour.
 * Defeating the mob gives a 50% chance of dropping that kit's GKit Gem.
 */
public class FallenHeroItem {

    public static final String PDC_KEY = "fallen_hero_bone";

    public static ItemStack createBone(FactionEnchantsPlugin plugin, String kitName) {
        String display = capitalize(kitName);
        ItemStack bone = new ItemStack(Material.BONE);
        ItemMeta meta = bone.getItemMeta();
        meta.setDisplayName("§c§l☠ " + display + " Fallen Hero Bone");
        meta.setLore(List.of(
                "§7Right-click to summon the Fallen Hero",
                "§7of the §c§l" + display + " §r§7gkit!",
                "§7",
                "§7Defeat the Fallen Hero for a",
                "§750% chance to earn the §b§l" + display + " Gem§7!"
        ));
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.STRING, kitName.toLowerCase());
        bone.setItemMeta(meta);
        return bone;
    }

    /** Returns the kit name stored in the bone, or {@code null} if not a fallen hero bone. */
    public static String getKitName(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.STRING);
    }

    public static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
