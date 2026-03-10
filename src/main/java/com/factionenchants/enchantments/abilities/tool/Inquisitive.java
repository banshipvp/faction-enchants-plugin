package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
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

    /**
     * Called by EnchantListener on block break.
     */
    public void onBlockBreak(Player player, BlockBreakEvent event, int level) {
        if (random.nextInt(100) < level * 10) {
            Collection<ItemStack> drops = event.getBlock().getDrops(player.getInventory().getItemInMainHand());
            for (ItemStack drop : drops) {
                player.getInventory().addItem(drop.clone());
            }
        }
    }
}
