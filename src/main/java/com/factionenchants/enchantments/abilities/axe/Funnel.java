package com.factionenchants.enchantments.abilities.axe;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Pummel III — Axe enchantment, ELITE tier.
 * Chance to slow nearby enemy players for a short period.
 */
public class Funnel extends CustomEnchantment {

    private static final Random random = new Random();

    public Funnel() {
        super("funnel", "Pummel", 3, EnchantTier.ELITE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Chance to slow nearby enemy players for a short period.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 20% per level chance to slow nearby enemies
        if (random.nextInt(100) >= level * 20) return;

        double radius = 4.0 + level;
        for (Entity nearby : attacker.getNearbyEntities(radius, radius, radius)) {
            if (nearby instanceof Player nearbyPlayer && !nearbyPlayer.equals(attacker)) {
                nearbyPlayer.addPotionEffect(new PotionEffect(
                        PotionEffectType.SLOW, 60, level - 1, false, true));
            }
        }
    }
}
