package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Divine Immolation – Sword/Axe enchantment, ELITE tier.
 * Chance to set the target ablaze on hit.
 */
public class DivineImmolation extends CustomEnchantment {

    private static final Random random = new Random();

    public DivineImmolation() {
        super("divine_immolation", "Divine Immolation", 5, EnchantTier.ELITE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Chance to set the target ablaze on hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 12) {
            target.setFireTicks(level * 40);
        }
    }
}
