package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for the White Scroll custom item.
 *
 * Applying it to gear adds "§f✦ PROTECTED" lore + a PDC marker.
 * When that gear would be destroyed by a book application, the scroll
 * absorbs the hit and the protection is removed instead.
 */
public class WhiteScrollItem {

    /** PDC key placed on the White Scroll item itself. */
    private static final String PDC_KEY         = "white_scroll";
    /** PDC key placed on protected gear. */
    private static final String PROTECTED_KEY   = "has_whitescroll";
    /** Exact lore line appended to protected gear. */
    public  static final String PROTECTED_LORE  = "§f✦ PROTECTED";

    // ── Factory ───────────────────────────────────────────────────────────────

    public static ItemStack create(FactionEnchantsPlugin plugin) {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§f§lWhite Scroll");
        meta.setLore(List.of(
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Apply to any piece of gear,",
                "§7weapon, or tool to grant it",
                "§fprotection §7against being",
                "§7destroyed by an enchant book.",
                "§7",
                "§7When a book would destroy the",
                "§7item, the §fWhite Scroll §7absorbs",
                "§7the hit and is consumed instead.",
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§eHold on cursor, click gear to apply"
        ));
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    // ── Checks ────────────────────────────────────────────────────────────────

    /** Returns true if the given item is a White Scroll. */
    public static boolean isWhiteScroll(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE);
    }

    /** Returns true if the given gear item has whitescroll protection active. */
    public static boolean isProtected(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PROTECTED_KEY), PersistentDataType.BYTE);
    }

    // ── Mutations ─────────────────────────────────────────────────────────────

    /**
     * Applies whitescroll protection to a gear item in-place.
     * Adds the PDC marker and appends PROTECTED_LORE to the bottom.
     */
    public static void applyProtection(FactionEnchantsPlugin plugin, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PROTECTED_KEY), PersistentDataType.BYTE, (byte) 1);
        List<String> lore = meta.hasLore()
                ? new ArrayList<>(meta.getLore())
                : new ArrayList<>();
        lore.removeIf(line -> line.equals(PROTECTED_LORE)); // de-duplicate
        lore.add(PROTECTED_LORE);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Removes whitescroll protection from a gear item in-place.
     * Strips the PDC marker and the PROTECTED_LORE line.
     */
    public static void removeProtection(FactionEnchantsPlugin plugin, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer()
                .remove(new NamespacedKey(plugin, PROTECTED_KEY));
        if (meta.hasLore()) {
            List<String> lore = new ArrayList<>(meta.getLore());
            lore.removeIf(line -> line.equals(PROTECTED_LORE));
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
    }
}
