package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Poison III — Sword enchantment, ELITE tier.
 * A chance of giving the poison effect on hit.
 */
public class Poison extends CustomEnchantment {

    private static final Random random = new Random();

    public Poison() {
        super("poison", "Poison", 3, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance of giving the poison effect.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 20% per level proc chance (max 60% at level 3)
        if (random.nextInt(100) < level * 20) {
            target.addPotionEffect(new PotionEffect(
                    PotionEffectType.POISON, 60 + level * 20, 0, false, true));
        }
    }
}
