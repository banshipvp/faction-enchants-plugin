package com.factionenchants.enchantments.abilities.misc;

import com.factionenchants.enchantments.CustomEnchantment;

/**
 * Trophy Seeker – Fishing Rod enchantment, ELITE tier.
 * Increases fishing luck on catch.
 * Logic handled in FishingListener.
 */
public class TrophySeeker extends CustomEnchantment {

    public TrophySeeker() {
        super("trophy_seeker", "Trophy Seeker", 5, EnchantTier.ELITE, ApplicableGear.FISHING_ROD);
    }

    @Override
    public String getDescription() {
        return "Increases luck when catching fish.";
    }
}
