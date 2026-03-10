package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Factory for Randomization Scrolls (Ultimate / Legendary / Godly).
 *
 * Drag n' Drop onto a matching-tier enchantment book to reroll its
 * success and destroy rates.
 *
 * {@code targetTier} values:
 *   "ULTIMATE"  — applies only to Ultimate books
 *   "LEGENDARY" — applies only to Legendary books
 *   "ANY"       — applies to any tier book (Godly)
 */
public class RandomizationScrollItem {

    public static final String PDC_KEY      = "randomization_scroll";
    public static final String PDC_TIER_KEY = "randomization_scroll_tier";

    // ── Factories ─────────────────────────────────────────────────────────────

    public static ItemStack createUltimate(FactionEnchantsPlugin plugin) {
        return create(plugin,
                "§e§lUltimate Randomization Scroll",
                "§eUltimate",
                "ULTIMATE",
                List.of(
                        "§7Apply to a(n) §eUltimate §7enchantment book",
                        "§7to reroll the success and destroy rates.",
                        "",
                        "§7Drag n' Drop onto §eenchantment book §7to apply.",
                        "",
                        "§6§lObtainable From:",
                        "§8✦ §7/envoys: §fUnique"
                ));
    }

    public static ItemStack createLegendary(FactionEnchantsPlugin plugin) {
        return create(plugin,
                "§6§lLegendary Randomization Scroll",
                "§6Legendary",
                "LEGENDARY",
                List.of(
                        "§7Apply to a(n) §6Legendary §7enchantment book",
                        "§7to reroll the success and destroy rates.",
                        "",
                        "§7Drag n' Drop onto §eenchantment book §7to apply.",
                        "",
                        "§6§lObtainable From:",
                        "§8✦ §7/envoys: §fElite",
                        "§8✦ §7/kits: §fSupernova"
                ));
    }

    public static ItemStack createGodly(FactionEnchantsPlugin plugin) {
        return create(plugin,
                "§c§lGodly Randomization Scroll",
                "§cANY",
                "ANY",
                List.of(
                        "§7Apply to a(n) §cANY §7enchantment book",
                        "§7to reroll the success and destroy rates.",
                        "",
                        "§7Drag n' Drop onto §eenchantment book §7to apply.",
                        "",
                        "§6§lObtainable From:",
                        "§8✦ §7/envoys: §fUltimate",
                        "§8✦ §7/kits: §fHeroic, Interstellar, Supernova, Hyperdrive, Lucky /vkit",
                        "§8✦ §7/nether: §fFortress Chests",
                        "§8✦ §7/pquests: §fQuest Shop",
                        "§8✦ §7/ruins: §fGuardians",
                        "§8✦ §7/spacechests: §fSimple, Unique, Elite, Ultimate, Legendary",
                        "§8✦ §7/supernova: §fBlack Market",
                        "§8✦ §7/trials: §fRecruit, Apprentice, Master",
                        "§8✦ §7/xpshop"
                ));
    }

    // ── Checks ────────────────────────────────────────────────────────────────

    public static boolean isRandomizationScroll(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE);
    }

    /** Returns "ULTIMATE", "LEGENDARY", or "ANY"; null if not a randomization scroll. */
    public static String getTargetTier(FactionEnchantsPlugin plugin, ItemStack item) {
        if (!isRandomizationScroll(plugin, item)) return null;
        return item.getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey(plugin, PDC_TIER_KEY), PersistentDataType.STRING);
    }

    // ── Internal builder ──────────────────────────────────────────────────────

    private static ItemStack create(FactionEnchantsPlugin plugin,
                                    String displayName,
                                    String tierDisplay,
                                    String tierKey,
                                    List<String> lore) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        var pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, PDC_KEY), PersistentDataType.BYTE, (byte) 1);
        pdc.set(new NamespacedKey(plugin, PDC_TIER_KEY), PersistentDataType.STRING, tierKey);
        item.setItemMeta(meta);
        return item;
    }
}
