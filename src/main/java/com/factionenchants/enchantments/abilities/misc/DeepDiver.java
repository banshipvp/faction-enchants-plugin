package com.factionenchants.enchantments.abilities.misc;

import com.factionenchants.enchantments.CustomEnchantment;

/**
 * DeepDiver V — Fishing Rod Enchantment.
 * Increases chance to fish treasure while fishing by up to +5% per level.
 * Applied in FishingListener when the state is CAUGHT_FISH.
 */
public class DeepDiver extends CustomEnchantment {

    public DeepDiver() {
        super("deep_diver", "Deep Diver", 5, EnchantTier.LEGENDARY, ApplicableGear.FISHING_ROD);
    }

    @Override
    public String getDescription() {
        return "Increases chance to fish treasure while fishing by up to +5%.";
    }
}
