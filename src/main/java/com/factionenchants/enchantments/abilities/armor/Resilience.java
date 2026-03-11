package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Resilience IV — Helmet enchantment, ELITE tier.
 * Increases HP of your NPC if you get combat logged by 15% per level.
 * Also has a chance to negate 50% of damage from incoming attacks that would normally kill you.
 */
public class Resilience extends CustomEnchantment {

    private static final Random random = new Random();

    public Resilience() {
        super("resilience", "Resilience", 4, EnchantTier.ELITE, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Chance to negate 50% of damage from attacks that would normally kill you.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double remainingHealth = defender.getHealth() - event.getFinalDamage();
        if (remainingHealth <= 0) {
            // 50% chance to negate half the lethal damage
            if (random.nextInt(100) < 50) {
                event.setDamage(event.getDamage() * 0.5);
            }
        }
    }
}
