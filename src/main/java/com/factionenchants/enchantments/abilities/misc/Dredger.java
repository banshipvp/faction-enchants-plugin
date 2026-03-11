package com.factionenchants.enchantments.abilities.misc;

import com.factionenchants.enchantments.CustomEnchantment;

/**
 * Dredger – Fishing Rod enchantment, UNIQUE tier.
 * Chance to grant double XP on fish catch.
 * Logic handled in FishingListener.
 */
public class Dredger extends CustomEnchantment {

    public Dredger() {
        super("dredger", "Dredger", 5, EnchantTier.UNIQUE, ApplicableGear.FISHING_ROD);
    }

    @Override
    public String getDescription() {
        return "Chance to grant double XP when catching fish.";
    }
}
