package com.factionenchants.enchantments.abilities.misc;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;

import java.util.Map;

/**
 * Sizzle V — Fishing Rod enchantment (LEGENDARY).
 * Chance to instantly cook fish upon catching them.
 * At max level (V), all fish caught are cooked.
 *
 * The actual fishing logic is handled by {@link com.factionenchants.listeners.FishingListener}
 * which checks for this enchant on the rod.
 *
 * Proc chance: level * 20% (20%, 40%, 60%, 80%, 100% at level V).
 */
public class Sizzle extends CustomEnchantment {

    /** Maps raw fish items to their cooked equivalents. */
    public static final Map<Material, Material> COOK_MAP = Map.of(
            Material.COD,   Material.COOKED_COD,
            Material.SALMON, Material.COOKED_SALMON
    );

    public Sizzle() {
        super("sizzle", "Sizzle", 5, EnchantTier.LEGENDARY, ApplicableGear.FISHING_ROD);
    }

    @Override
    public String getDescription() {
        return "Chance to instantly cook fish upon catching them. At max level, all fish caught are cooked.";
    }

    /**
     * Returns the proc chance (0.0–1.0) for the given level.
     */
    public static double getProcChance(int level) {
        return Math.min(level * 0.20, 1.0);
    }
}
