package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Factory for the Transmog Scroll.
 *
 * Organizes custom enchants on an item by rarity (Simple → Mastery) and
 * appends the enchant count to the item's display name.
 * Applied by holding scroll and clicking onto gear (inventory click).
 */
public class TransmogScrollItem {

    public static final String PDC_KEY = "transmog_scroll";

    // ── Factory ───────────────────────────────────────────────────────────────

    public static ItemStack create(FactionEnchantsPlugin plugin) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lTransmog Scroll");
        meta.setLore(List.of(
                "§7Organizes enchants by §erarity §7on item",
                "§7and adds the §elore count §7to name.",
                "",
                "§7Place scroll on item to apply.",
                "",
                "§6§lObtainable From:",
                "§8✦ §7/conquests",
                "§8✦ §7/kits: §fHeroic, Supernova",
                "§8✦ §7/nether: §fFortress Chests",
                "§8✦ §7/spacechests: §fSimple, Unique, Elite, Ultimate",
                "§8✦ §7/trials: §fRecruit",
                "§8✦ §7/xpshop"
        ));
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    // ── Checks ────────────────────────────────────────────────────────────────

    public static boolean isTransmogScroll(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE);
    }
}
