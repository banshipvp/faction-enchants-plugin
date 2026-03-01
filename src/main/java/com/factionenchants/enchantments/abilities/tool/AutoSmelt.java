package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;

public class AutoSmelt extends CustomEnchantment {

    private static final Map<Material, Material> SMELT_MAP = new EnumMap<>(Material.class);

    static {
        // Ores → ingots/gems
        SMELT_MAP.put(Material.IRON_ORE,          Material.IRON_INGOT);
        SMELT_MAP.put(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT);
        SMELT_MAP.put(Material.GOLD_ORE,           Material.GOLD_INGOT);
        SMELT_MAP.put(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT);
        SMELT_MAP.put(Material.COPPER_ORE,         Material.COPPER_INGOT);
        SMELT_MAP.put(Material.DEEPSLATE_COPPER_ORE, Material.COPPER_INGOT);
        SMELT_MAP.put(Material.NETHER_GOLD_ORE,    Material.GOLD_INGOT);
        SMELT_MAP.put(Material.ANCIENT_DEBRIS,     Material.NETHERITE_SCRAP);
        // Raw metals
        SMELT_MAP.put(Material.RAW_IRON,           Material.IRON_INGOT);
        SMELT_MAP.put(Material.RAW_GOLD,           Material.GOLD_INGOT);
        SMELT_MAP.put(Material.RAW_COPPER,         Material.COPPER_INGOT);
        // Other smeltables
        SMELT_MAP.put(Material.COBBLESTONE,        Material.STONE);
        SMELT_MAP.put(Material.SAND,               Material.GLASS);
        SMELT_MAP.put(Material.GRAVEL,             Material.FLINT);
        SMELT_MAP.put(Material.NETHERRACK,         Material.NETHER_BRICK);
        SMELT_MAP.put(Material.CLAY,               Material.TERRACOTTA);
    }

    public AutoSmelt() {
        super("autosmelt", "Auto Smelt", 3, EnchantTier.SIMPLE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Automatically smelts ores into ingots when mined\nand places them directly in your inventory.";
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
    }

    /** Returns the smelted result of a given block material, or null if not smeltable. */
    public static Material getSmeltResult(Material blockType) {
        return SMELT_MAP.get(blockType);
    }
}
