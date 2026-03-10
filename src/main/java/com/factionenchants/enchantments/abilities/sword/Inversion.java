package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Inversion IV — Sword enchantment (LEGENDARY).
 * While holding this sword, incoming damage has a (level * 15)% chance to be
 * blocked entirely, and instead heals the holder for 1–3 HP (half-hearts).
 *
 * Requires CombatListener to scan the defender's held weapon for onHurtBy procs
 * (already done after each armor piece scan).
 */
public class Inversion extends CustomEnchantment {

    public Inversion() {
        super("inversion", "Inversion", 4, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Damage dealt to you has a chance to be blocked and heal you for 1-3 HP instead.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double procChance = level * 0.15;
        if (Math.random() >= procChance) return;

        // Block the damage
        event.setDamage(0.0);

        // Heal 1–3 half-hearts
        double heal = 1.0 + Math.random() * 2.0;
        double newHealth = Math.min(defender.getHealth() + heal, defender.getMaxHealth());
        defender.setHealth(newHealth);
    }
}
