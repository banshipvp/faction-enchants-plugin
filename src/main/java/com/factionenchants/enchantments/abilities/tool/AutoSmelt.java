package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;

import java.util.Map;

/**
 * AutoSmelt – Tool enchantment, ELITE tier.
 * Automatically smelts ore drops when mining.
 */
public class AutoSmelt extends CustomEnchantment {

    private static final Map<Material, Material> SMELT_MAP = Map.ofEntries(
            Map.entry(Material.IRON_ORE,           Material.IRON_INGOT),
            Map.entry(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT),
            Map.entry(Material.RAW_IRON,           Material.IRON_INGOT),
            Map.entry(Material.GOLD_ORE,           Material.GOLD_INGOT),
            Map.entry(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT),
            Map.entry(Material.NETHER_GOLD_ORE,    Material.GOLD_INGOT),
            Map.entry(Material.RAW_GOLD,           Material.GOLD_INGOT),
            Map.entry(Material.COPPER_ORE,         Material.COPPER_INGOT),
            Map.entry(Material.DEEPSLATE_COPPER_ORE, Material.COPPER_INGOT),
            Map.entry(Material.RAW_COPPER,         Material.COPPER_INGOT),
            Map.entry(Material.ANCIENT_DEBRIS,     Material.NETHERITE_SCRAP),
            Map.entry(Material.COBBLESTONE,        Material.STONE),
            Map.entry(Material.SAND,               Material.GLASS),
            Map.entry(Material.GRAVEL,             Material.FLINT)
    );

    public AutoSmelt() {
        super("auto_smelt", "Auto Smelt", 1, EnchantTier.SIMPLE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Automatically smelts ore drops when mining.";
    }

    /**
     * Returns the smelted result of the given material, or null if it cannot be smelted.
     */
    public static Material getSmeltResult(Material input) {
        return SMELT_MAP.get(input);
    }
}
