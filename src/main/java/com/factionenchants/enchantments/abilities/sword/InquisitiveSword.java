package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Inquisitive IV — Sword enchantment (LEGENDARY).
 * Multiplies the experience dropped by mobs killed with this sword.
 * XP multiplier: 1 + level (level 1 = ×2, level 4 = ×5).
 */
public class InquisitiveSword extends CustomEnchantment {

    public InquisitiveSword() {
        super("inquisitive_sword", "Inquisitive", 4, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Increases EXP drops from mobs.";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        // Bonus XP = level * (5 to 10 random xp)
        int bonusXp = level * (5 + (int) (Math.random() * 6));
        killer.getWorld().spawn(victim.getLocation(), org.bukkit.entity.ExperienceOrb.class, orb -> orb.setExperience(bonusXp));
    }
}
