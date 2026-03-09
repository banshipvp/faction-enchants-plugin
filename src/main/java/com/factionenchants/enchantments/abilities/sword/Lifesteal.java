package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Lifesteal — Sword enchantment.
 * A chance per hit to regain health. Proc chance and heal amount scale with level.
 */
public class Lifesteal extends CustomEnchantment {

    private final Random random = new Random();

    public Lifesteal() {
        super("lifesteal", "Lifesteal", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to regain health when attacking. Proc chance and heal scale with level.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Proc chance: 10% per level (10–50%)
        if (random.nextInt(100) >= level * 10) return;
        double heal = 1.0 + level * 0.5;
        attacker.setHealth(Math.min(attacker.getHealth() + heal, attacker.getMaxHealth()));
    }
}
