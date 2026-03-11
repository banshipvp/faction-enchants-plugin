package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Shockwave V — Chestplate enchantment, ELITE tier.
 * Chance to push back your attacker when you take damage.
 */
public class Shockwave extends CustomEnchantment {

    private static final Random random = new Random();

    public Shockwave() {
        super("shockwave", "Shockwave", 5, EnchantTier.ELITE, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "10% chance per level to knockback your attacker (knockback scales with level).";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // 10% per level proc chance (max 50% at level 5)
        if (random.nextInt(100) >= level * 10) return;

        Vector direction = attacker.getLocation().toVector()
                .subtract(defender.getLocation().toVector())
                .normalize()
                .multiply(1.5 + level * 0.5);
        direction.setY(0.35);
        attacker.setVelocity(direction);
    }
}
