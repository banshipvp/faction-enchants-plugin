package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Master Inquisitive – Sword enchantment, HEROIC tier.
 * Gives significantly more XP than the base Inquisitive sword enchant.
 * Requires Inquisitive (sword) at max level (IV) to be applied.
 * XP bonus: level * (10-20 random XP), which is roughly double Inquisitive.
 */
public class MasterInquisitive extends CustomEnchantment {

    public MasterInquisitive() {
        super("master_inquisitive", "Master Inquisitive", 5, EnchantTier.HEROIC, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Massively increases EXP drops from mobs beyond standard Inquisitive.";
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "inquisitive_sword";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        // Master Inquisitive gives ~2x the XP of InquisitiveSword
        // InquisitiveSword gives level * (5-10), so this gives level * (10-20)
        int bonusXp = level * (10 + (int) (Math.random() * 11));
        killer.getWorld().spawn(victim.getLocation(), org.bukkit.entity.ExperienceOrb.class, orb -> orb.setExperience(bonusXp));
    }
}
