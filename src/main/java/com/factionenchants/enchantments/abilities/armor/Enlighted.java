package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Enlighted III — Armor enchantment (LEGENDARY).
 * Gives a (level * 20)% chance to heal the wearer while they are taking damage.
 * Heal amount: 0.5 × level hearts (half-hearts per level).
 */
public class Enlighted extends CustomEnchantment {

    public Enlighted() {
        super("enlighted", "Enlighted", 3, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "20% chance per level to heal 0.5 hearts per level when taking damage.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double procChance = level * 0.20; // 20% per level, up to 60% at III
        if (Math.random() >= procChance) return;

        double heal = level * 0.5; // heal in half-hearts (0.5 per level)
        double newHealth = Math.min(defender.getHealth() + heal, defender.getMaxHealth());
        defender.setHealth(newHealth);
    }
}
