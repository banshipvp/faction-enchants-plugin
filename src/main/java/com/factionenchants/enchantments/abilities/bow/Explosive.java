package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Explosive V — Bow enchantment, UNIQUE tier.
 * Arrows cause an explosion at the point of impact.
 */
public class Explosive extends CustomEnchantment {

    public Explosive() {
        super("explosive", "Explosive", 5, EnchantTier.UNIQUE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Arrows explode on impact.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        Location loc = target.getLocation();
        // Power scales with level (0.5–2.5); no fire, no block damage at low levels
        float power = 0.5f * level;
        boolean breakBlocks = level >= 4;
        loc.getWorld().createExplosion(loc, power, false, breakBlocks, shooter);
    }
}
