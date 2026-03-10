package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;

import java.util.Random;

/**
 * Hardened – Armor enchantment, ELITE tier.
 * Chance to negate armor durability damage on hit.
 */
public class Hardened extends CustomEnchantment {

    private static final Random random = new Random();

    public Hardened() {
        super("hardened", "Hardened", 5, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Chance to negate armor durability damage.";
    }

    /**
     * Called from PlayerItemDamageEvent in EnchantListener.
     * @return true if the durability damage should be cancelled this time.
     */
    public boolean shouldCancelDamage(int level) {
        return random.nextInt(100) < level * 15; // 15–75% chance
    }
}
