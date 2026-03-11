package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Cactus II — Armor enchantment, ELITE tier.
 * Injures your attacker but does not affect your durability.
 */
public class Cactus extends CustomEnchantment {

    public Cactus() {
        super("cactus", "Cactus", 2, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Injures your attacker but does not affect your durability.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof LivingEntity livingAttacker)) return;
        // Reflect 1.5 damage per level back to attacker (thorn-like, without damaging defender's armor)
        livingAttacker.damage(1.5 * level, defender);
    }
}
