package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Factory for the Vanilla Black Scroll.
 *
 * Removes a random *vanilla* enchantment from an item and converts it into a
 * 95% success enchanted book. Only extracts obtainable vanilla enchants.
 * Applied by holding and clicking onto gear (inventory click).
 */
public class VanillaBlackScrollItem {

    public static final String PDC_KEY = "vanilla_black_scroll";

    // ── Factory ───────────────────────────────────────────────────────────────

    public static ItemStack create(FactionEnchantsPlugin plugin) {
        ItemStack item = new ItemStack(Material.INK_SAC);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§7§lVanilla Black Scroll");
        meta.setLore(List.of(
                "§7Removes a random vanilla",
                "§7enchantment from an item and",
                "§7converts it into a §f95% §7success book.",
                "§7⚡ §7Only extracts §fobtainable §7enchants",
                "",
                "§7Place scroll on item to extract.",
                "",
                "§6§lObtainable From:",
                "§8✦ §7/outposts: §fVanilla"
        ));
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    // ── Checks ────────────────────────────────────────────────────────────────

    public static boolean isVanillaBlackScroll(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE);
    }
}
