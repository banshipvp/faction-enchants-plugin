package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Random;

/**
 * Master Inquisitive – Pickaxe enchantment, ELITE tier.
 * Higher chance and triple drops from mined blocks.
 */
public class MasterInquisitive extends CustomEnchantment {

    private static final Random random = new Random();

    public MasterInquisitive() {
        super("master_inquisitive", "Master Inquisitive", 5, EnchantTier.HEROIC, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Higher chance to triple the drops from the mined block.";
    }

    /**
     * Called by EnchantListener on block break.
     */
    public void onBlockBreak(Player player, BlockBreakEvent event, int level) {
        if (random.nextInt(100) < level * 12) {
            ItemStack tool = player.getInventory().getItemInMainHand();
            Collection<ItemStack> drops = event.getBlock().getDrops(tool);
            for (ItemStack drop : drops) {
                ItemStack bonus = drop.clone();
                bonus.setAmount(drop.getAmount() * 2);
                player.getInventory().addItem(bonus);
            }
        }
    }
}
