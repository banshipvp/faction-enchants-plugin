package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;

/**
 * Nimble V — Boots enchantment, UNIQUE tier.
 * Increases mcMMO XP gained in Acrobatics while equipped.
 * XP boost logic is handled by McMMOListener (+15% per level, max +75% at V).
 */
public class Nimble extends CustomEnchantment {

    public Nimble() {
        super("nimble", "Nimble", 5, EnchantTier.UNIQUE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Increases mcMMO XP gained in Acrobatics while equipped.";
    }
}
