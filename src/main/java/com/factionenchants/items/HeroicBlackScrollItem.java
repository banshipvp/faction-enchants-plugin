package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Factory for the Heroic Black Scroll.
 *
 * Removes a random enchantment (including Heroic-tier) from an item and
 * converts it into a 10–25% success book.
 * Applied by holding the scroll and clicking onto gear (inventory click).
 */
public class HeroicBlackScrollItem {

    public static final String PDC_KEY = "heroic_black_scroll";

    // ── Factory ───────────────────────────────────────────────────────────────

    public static ItemStack create(FactionEnchantsPlugin plugin) {
        ItemStack item = new ItemStack(Material.INK_SAC);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§d§lHeroic Black Scroll");
        meta.setLore(List.of(
                "§7Removes a random enchantment",
                "§7from an item and converts",
                "§7it into a §d10%§7-§d25% §7success book.",
                "§d⚡ §7Chance to extract §dHeroic Enchantments",
                "",
                "§7Place scroll on item to extract.",
                "",
                "§6§lObtainable From:",
                "§8✦ §7/bah",
                "§8✦ §7/citadel",
                "§8✦ §7/conquests",
                "§8✦ §7/dungeons: §fAbandoned Spaceship, Destroyed Outpost, Planet Nul, Time Warp",
                "§8✦ §7/end: §fElite Ender Monsters, Timeless Dragon",
                "§8✦ §7/envoys: §fLegendary",
                "§8✦ §7/kits: §fSupernova, Hyperdrive, Lucky /vkit",
                "§8✦ §7/koth",
                "§8✦ §7/ruins: §fElite Guardians, Elder Elite Guardians, Leviathan",
                "§8✦ §7/slot: §fMeta Spin",
                "§8✦ §7/strongholds: §fInfernal, Frozen, Abyssal",
                "§8✦ §7/tournaments: §f1st place",
                "§8✦ §7/trials: §fHardcore, Demonic",
                "§8✦ §7/xpshop"
        ));
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    // ── Checks ────────────────────────────────────────────────────────────────

    public static boolean isHeroicBlackScroll(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE);
    }
}
