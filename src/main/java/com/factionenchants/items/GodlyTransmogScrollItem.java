package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Factory for the Godly Transmog Scroll.
 *
 * Opens an interactive GUI that lets the player choose the display order of
 * custom enchantments on a piece of equipment.
 * Applied by holding scroll and clicking onto gear (inventory click).
 */
public class GodlyTransmogScrollItem {

    public static final String PDC_KEY = "godly_transmog_scroll";

    // ── Factory ───────────────────────────────────────────────────────────────

    public static ItemStack create(FactionEnchantsPlugin plugin) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6§lGodly Transmog Scroll");
        meta.setLore(List.of(
                "§7Apply to equipment to §6customize",
                "§7the order of §6custom enchantments",
                "§7displayed on the item.",
                "",
                "§7Place scroll on item to apply.",
                "",
                "§6§lObtainable From:",
                "§8✦ §7/conquests",
                "§8✦ §7/kits: §fHeroic, Interstellar, Supernova",
                "§8✦ §7/koth",
                "§8✦ §7/slot: §fCredit Shop",
                "§8✦ §7/spacechests: §fSimple, Unique, Elite, Ultimate",
                "§8✦ §7/strongholds: §fInfernal, Frozen, Abyssal",
                "§8✦ §7/supernova: §fBlack Market",
                "§8✦ §7/xpshop"
        ));
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    // ── Checks ────────────────────────────────────────────────────────────────

    public static boolean isGodlyTransmogScroll(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE);
    }
}
