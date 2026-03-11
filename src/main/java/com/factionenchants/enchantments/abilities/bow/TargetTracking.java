package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Target Tracking I — Bow enchantment, UNIQUE tier.
 * Automatically /f focus any player you hit with an arrow.
 */
public class TargetTracking extends CustomEnchantment {

    public TargetTracking() {
        super("targettracking", "Target Tracking", 1, EnchantTier.SIMPLE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Automatically /f focus any target you hit.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player targetPlayer)) return;
        shooter.performCommand("f focus " + targetPlayer.getName());
    }
}
