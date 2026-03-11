package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Lightning III — Bow enchantment, UNIQUE tier.
 * A chance to strike lightning where you strike.
 */
public class Lightning extends CustomEnchantment {

    private static final Random random = new Random();

    public Lightning() {
        super("lightning", "Lightning", 3, EnchantTier.SIMPLE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "25% chance per level to strike lightning on target hit with arrow.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 25% chance per level (25%, 50%, 75% at levels I–III)
        if (random.nextInt(100) < level * 25) {
            target.getWorld().strikeLightning(target.getLocation());
        }
    }
}
