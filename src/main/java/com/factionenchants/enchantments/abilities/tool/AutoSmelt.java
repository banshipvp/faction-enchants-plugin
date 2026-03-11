package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
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

    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = block.getDrops(tool);
        boolean anySmelted = false;
        for (ItemStack drop : drops) {
            Material smelted = getSmeltResult(drop.getType());
            if (smelted != null) {
                anySmelted = true;
                ItemStack smeltedItem = new ItemStack(smelted, drop.getAmount());
                HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(smeltedItem);
                for (ItemStack leftover : overflow.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), leftover);
                }
            } else {
                // Drop non-smeltable items normally
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
        if (anySmelted) {
            event.setDropItems(false);
        }
    }
}
