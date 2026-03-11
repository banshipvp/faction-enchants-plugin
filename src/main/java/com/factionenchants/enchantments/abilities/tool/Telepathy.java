package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;

/**
 * Telepathy IV — Tool enchantment, UNIQUE tier.
 * Automatically places blocks broken by tools in your inventory.
 */
public class Telepathy extends CustomEnchantment {

    public Telepathy() {
        super("telepathy", "Telepathy", 4, EnchantTier.UNIQUE, ApplicableGear.TOOL_ALL);
    }

    @Override
    public String getDescription() {
        return "Automatically places blocks broken by tools in your inventory.";
    }

    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = block.getDrops(tool);
        event.setDropItems(false);
        for (ItemStack drop : drops) {
            HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(drop);
            for (ItemStack leftover : overflow.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), leftover);
            }
        }
    }
}
