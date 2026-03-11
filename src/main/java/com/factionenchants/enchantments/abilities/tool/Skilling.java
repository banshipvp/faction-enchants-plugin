package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;

/**
 * Skilling X — Tool enchantment, UNIQUE tier.
 * Increases mcMMO XP gained in all GATHERING skills while equipped.
 * XP boost logic is handled by McMMOListener (+10% per level, max +100% at X).
 */
public class Skilling extends CustomEnchantment {

    public Skilling() {
        super("skilling", "Skilling", 10, EnchantTier.UNIQUE, ApplicableGear.TOOL_ALL);
    }

    @Override
    public String getDescription() {
        return "Increases mcMMO XP gained in all GATHERING skills while equipped.";
    }
}
