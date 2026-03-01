package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Inquisitive extends CustomEnchantment {

    public Inquisitive() {
        super("inquisitive", "Inquisitive", 5, EnchantTier.LEGENDARY, ApplicableGear.TOOL_ALL);
    }

    @Override
    public String getDescription() {
        return "Grants bonus experience when mining blocks.";
    }

    // Called by EnchantListener on BlockBreakEvent
    public void onBlockBreak(Player player, BlockBreakEvent event, int level) {
        event.setExpToDrop(event.getExpToDrop() + level * 2);
    }
}
