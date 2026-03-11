package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Trap III — Sword enchantment, ELITE tier.
 * A chance to give a buffed slowness effect on hit.
 */
public class Trap extends CustomEnchantment {

    private static final Random random = new Random();

    public Trap() {
        super("trap", "Trap", 3, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to give a buffed slowness effect.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 25% chance per level (25%, 50%, 75%)
        if (random.nextInt(100) >= level * 25) return;

        // Slowness amplitude scales with level: level I = Slowness III, II = IV, III = V
        int amplifier = level + 1;
        // Duration: 2 seconds base + 1 second per level (40, 60, 80 ticks)
        int durationTicks = 40 + (level * 20);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, durationTicks, amplifier, false, true));
    }
}
