package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;

/**
 * Obsidian Breaker – Pickaxe enchantment, ELITE tier.
 * Allows breaking obsidian, crying obsidian, and respawn anchors
 * via left-click (handled in EnchantListener).
 */
public class ObsidianBreaker extends CustomEnchantment {

    public ObsidianBreaker() {
        super("obsidian_breaker", "Obsidian Breaker", 3, EnchantTier.ELITE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Allows breaking obsidian-type blocks with left-click.";
    }
}
