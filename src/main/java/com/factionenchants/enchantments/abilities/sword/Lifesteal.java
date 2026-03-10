package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Lifesteal V — Sword enchantment (LEGENDARY).
 * Each melee hit has a (level * 15)% chance to restore health equal to
 * (level * 0.5) half-hearts. Healback is capped at max health.
 */
public class Lifesteal extends CustomEnchantment {

    public Lifesteal() {
        super("lifesteal", "Lifesteal", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to regain health when attacking.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double procChance = level * 0.15;
        if (Math.random() >= procChance) return;

        double heal = level * 0.5;
        double newHealth = Math.min(attacker.getHealth() + heal, attacker.getMaxHealth());
        attacker.setHealth(newHealth);
    }
}
