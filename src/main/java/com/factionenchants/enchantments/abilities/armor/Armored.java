package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Armored – Armor enchantment, LEGENDARY tier.
 * Reduces incoming damage based on armor enchant level.
 */
public class Armored extends CustomEnchantment {

    public Armored() {
        super("armored", "Armored", 5, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Reduces incoming damage by 3% per level (max 15% at level V).";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double reduction = level * 0.03; // 3% per level, max 15%
        event.setDamage(event.getDamage() * (1.0 - reduction));
    }
}
