package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MasterInquisitive extends CustomEnchantment {

    public MasterInquisitive() {
        super("master_inquisitive", "Master Inquisitive", 5, EnchantTier.HEROIC, ApplicableGear.TOOL_ALL);
    }

    @Override
    public String getDescription() {
        return "Massive bonus experience from mining.";
    }

    public void onBlockBreak(Player player, BlockBreakEvent event, int level) {
        event.setExpToDrop(event.getExpToDrop() + level * 5);
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "inquisitive";
    }
}
