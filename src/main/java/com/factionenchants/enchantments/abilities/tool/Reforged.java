package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;

import java.util.Random;

/**
 * Reforged – Tool enchantment, ELITE tier.
 * Chance to negate tool durability damage on use.
 */
public class Reforged extends CustomEnchantment {

    private static final Random random = new Random();

    public Reforged() {
        super("reforged", "Reforged", 5, EnchantTier.ELITE, ApplicableGear.TOOL_ALL);
    }

    @Override
    public String getDescription() {
        return "Chance to negate tool durability damage.";
    }

    /**
     * Called from PlayerItemDamageEvent in EnchantListener.
     * @return true if the durability damage should be cancelled this time.
     */
    public boolean shouldCancelDamage(int level) {
        return random.nextInt(100) < level * 15; // 15–75% chance
    }
}
