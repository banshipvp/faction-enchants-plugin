package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

/**
 * Obsidian Destroyer V — Pickaxe enchantment, UNIQUE tier.
 * Each hit on obsidian has a chance to instantly break it.
 */
public class ObsidianDestroyer extends CustomEnchantment {

    private static final Random random = new Random();

    public ObsidianDestroyer() {
        super("obsidiandestroyer", "Obsidian Destroyer", 5, EnchantTier.UNIQUE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Chance to instantly break obsidian blocks.";
    }

    @Override
    public void onBlockDamage(Player player, Block block, int level, BlockDamageEvent event) {
        if (block.getType() != Material.OBSIDIAN && block.getType() != Material.CRYING_OBSIDIAN) return;
        // 20% chance per level (100% at V)
        if (random.nextInt(100) >= level * 20) return;

        ItemStack tool = player.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = block.getDrops(tool);
        block.setType(Material.AIR);
        for (ItemStack drop : drops) {
            HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(drop);
            for (ItemStack leftover : overflow.values()) {
                player.getWorld().dropItemNaturally(block.getLocation(), leftover);
            }
        }
    }
}
