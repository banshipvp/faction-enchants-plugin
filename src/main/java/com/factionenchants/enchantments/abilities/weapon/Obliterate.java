package com.factionenchants.enchantments.abilities.weapon;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

/**
 * Obliterate V — Weapon enchantment, UNIQUE tier.
 * Extreme knockback on hit.
 */
public class Obliterate extends CustomEnchantment {

    public Obliterate() {
        super("obliterate", "Obliterate", 5, EnchantTier.UNIQUE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Extreme knockback.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        Vector dir = target.getLocation().toVector()
                .subtract(attacker.getLocation().toVector())
                .normalize()
                .multiply(1.0 + level * 0.5);
        dir.setY(0.3 + level * 0.04);
        target.setVelocity(dir);
    }
}
