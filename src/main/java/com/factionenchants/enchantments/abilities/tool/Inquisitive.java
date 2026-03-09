package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Inquisitive — Sword enchantment.
 * Increases EXP dropped by mobs when you kill them.
 */
public class Inquisitive extends CustomEnchantment {

    public Inquisitive() {
        super("inquisitive", "Inquisitive", 4, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Increases EXP drops from mobs you kill.";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        // Drop bonus XP at the victim's location
        int bonus = level * 3;
        killer.giveExp(bonus);
    }

    // Keep block-break bonus for backwards compatibility if still applied to tools
    public void onBlockBreak(Player player, BlockBreakEvent event, int level) {
        event.setExpToDrop(event.getExpToDrop() + level * 2);
    }
}
