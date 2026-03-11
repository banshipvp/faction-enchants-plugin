package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;

import java.util.Map;

public class Fuse extends CustomEnchantment {

    public Fuse() {
        super("fuse", "Fuse", 1, EnchantTier.ULTIMATE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Allows Auto Smelt to apply to blocks destroyed by the Detonate enchantment.";
    }

    /** Maps raw ore/block types to their smelted output. */
    public static Material getSmeltResult(Material raw) {
        return SMELT_MAP.getOrDefault(raw, null);
    }

    private static final Map<Material, Material> SMELT_MAP = Map.ofEntries(
            Map.entry(Material.IRON_ORE,        Material.IRON_INGOT),
            Map.entry(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT),
            Map.entry(Material.RAW_IRON,        Material.IRON_INGOT),
            Map.entry(Material.GOLD_ORE,        Material.GOLD_INGOT),
            Map.entry(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT),
            Map.entry(Material.RAW_GOLD,        Material.GOLD_INGOT),
            Map.entry(Material.COPPER_ORE,      Material.COPPER_INGOT),
            Map.entry(Material.DEEPSLATE_COPPER_ORE, Material.COPPER_INGOT),
            Map.entry(Material.RAW_COPPER,      Material.COPPER_INGOT),
            Map.entry(Material.ANCIENT_DEBRIS,  Material.NETHERITE_SCRAP),
            Map.entry(Material.SAND,            Material.GLASS),
            Map.entry(Material.GRAVEL,          Material.FLINT),
            Map.entry(Material.COBBLESTONE,     Material.STONE),
            Map.entry(Material.STONE,           Material.SMOOTH_STONE),
            Map.entry(Material.CLAY,            Material.TERRACOTTA),
            Map.entry(Material.WET_SPONGE,      Material.SPONGE)
    );
}
