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
 * Factory for the Holy White Scroll item.
 *
 * Requires a White Scroll on the item before application.
 * When applied to armor/weapons, gives 100% chance of not losing
 * the blessed item on death. The protection lore and PDC flag are placed
 * on the gear; a PlayerDeathEvent listener drops everything except items
 * marked with has_holy_whitescroll.
 */
public class HolyWhiteScrollItem {

    private static final String PDC_KEY       = "holy_white_scroll";
    private static final String PROTECTED_KEY  = "has_holy_whitescroll";
    public  static final String PROTECTED_LORE = "§d✦ BLESSED (Keep on Death)";

    // ── Factory ───────────────────────────────────────────────────────────────

    public static ItemStack create(FactionEnchantsPlugin plugin) {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§d§lHoly White Scroll");
        meta.setLore(List.of(
                "§7A legendary /dungeon reward that",
                "§7can be applied to armor/weapons,",
                "§7gives a §d100% §7chance of not losing",
                "§7the blessed item on death.",
                "",
                "§eREQ: §fWhite Scroll",
                "§7(which will be consumed on application)",
                "",
                "§6§lObtainable From:",
                "§8✦ §7/bah",
                "§8✦ §7/citadel",
                "§8✦ §7/conquests",
                "§8✦ §7/dungeons: §fPlanet Nul, Time Warp",
                "§8✦ §7/end: §fElite Ender Monsters, Envoy, Timeless Dragon",
                "§8✦ §7/invasions: §fNether, Demonic, Dark Space",
                "§8✦ §7/kits: §fHyperdrive, Lucky /vkit",
                "§8✦ §7/koth",
                "§8✦ §7/minigames",
                "§8✦ §7/raid Outpost",
                "§8✦ §7/ruins: §fElite Guardians, Elder Elite Guardians, Leviathan",
                "§8✦ §7/slot: §fMeta Spin, Flash Sale",
                "§8✦ §7/spacechests: §fGodly",
                "§8✦ §7/strongholds: §fInfernal, Frozen, Abyssal",
                "§8✦ §7/tournaments: §f1st place",
                "§8✦ §7/trials: §fDemonic",
                "§8✦ §7/xpshop"
        ));
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    // ── Checks ────────────────────────────────────────────────────────────────

    public static boolean isHolyWhiteScroll(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE);
    }

    public static boolean isBlessed(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PROTECTED_KEY), PersistentDataType.BYTE);
    }

    // ── Mutations ─────────────────────────────────────────────────────────────

    /**
     * Blesses a gear item in-place.
     * Adds the PDC marker and the BLESSED lore line.
     */
    public static void applyBlessing(FactionEnchantsPlugin plugin, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PROTECTED_KEY), PersistentDataType.BYTE, (byte) 1);
        List<String> lore = meta.hasLore()
                ? new ArrayList<>(meta.getLore())
                : new ArrayList<>();
        lore.removeIf(line -> line.equals(PROTECTED_LORE));
        // Remove any white scroll protection lore first (parent req)
        lore.removeIf(line -> line.equals(WhiteScrollItem.PROTECTED_LORE));
        lore.add(PROTECTED_LORE);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Removes the blessing from a gear item in-place.
     */
    public static void removeBlessing(FactionEnchantsPlugin plugin, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(new NamespacedKey(plugin, PROTECTED_KEY));
        if (meta.hasLore()) {
            List<String> lore = new ArrayList<>(meta.getLore());
            lore.removeIf(line -> line.equals(PROTECTED_LORE));
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
    }
}
