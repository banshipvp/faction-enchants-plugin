package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;

/**
 * Telepathy – Tool enchantment, ELITE tier.
 * Teleports block drops directly into the player's inventory.
 * The actual teleportation logic is handled in EnchantListener.
 */
public class Telepathy extends CustomEnchantment {

    public Telepathy() {
        super("telepathy", "Telepathy", 1, EnchantTier.ELITE, ApplicableGear.TOOL_ALL);
    }

    @Override
    public String getDescription() {
        return "Block drops go directly into your inventory.";
    }
}
