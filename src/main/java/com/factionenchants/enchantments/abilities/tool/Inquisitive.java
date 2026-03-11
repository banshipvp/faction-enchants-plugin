package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Random;

/**
 * Inquisitive – Pickaxe enchantment, UNIQUE tier.
 * Chance to double the drops from the mined block.
 */
public class Inquisitive extends CustomEnchantment {

    private static final Random random = new Random();

    public Inquisitive() {
        super("inquisitive", "Inquisitive", 5, EnchantTier.UNIQUE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Chance to double the drops from the mined block.";
    }

    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if (random.nextInt(100) < level * 10) {
            Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
            for (ItemStack drop : drops) {
                player.getInventory().addItem(drop.clone());
            }
        }
    }
}
