package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;

/**
 * Quick Reeler – Fishing Rod enchantment, UNIQUE tier.
 * Reduces the wait time when fishing.
 * The actual wait-time reduction is applied in FishingListener.
 */
public class QuickReeler extends CustomEnchantment {

    public QuickReeler() {
        super("quick_reeler", "Quick Reeler", 5, EnchantTier.UNIQUE, ApplicableGear.FISHING_ROD);
    }

    @Override
    public String getDescription() {
        return "Reduces the wait time when fishing.";
    }
}
