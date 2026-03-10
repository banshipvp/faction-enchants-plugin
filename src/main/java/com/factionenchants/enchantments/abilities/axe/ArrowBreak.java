package com.factionenchants.enchantments.abilities.axe;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Arrow Break — Axe enchantment, ULTIMATE tier, max level VI.
 * Chance for arrows to bounce off and do no damage to you whenever
 * you are wielding an axe with this enchantment.
 *
 * Activation chance: level × 12% (12%–72%).
 * Handled by {@link com.factionenchants.listeners.ArrowBreakListener}
 * which checks the player's main-hand item for this enchantment.
 */
public class ArrowBreak extends CustomEnchantment {

    private static final Random random = new Random();

    public ArrowBreak() {
        super("arrow_break", "Arrow Break", 6, EnchantTier.ULTIMATE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Chance for arrows to bounce off and do no damage to you whenever you are wielding an axe with this enchantment on it.";
    }

    /**
     * Returns true if this proc should cancel the incoming arrow damage.
     * Called directly from {@link com.factionenchants.listeners.ArrowBreakListener}.
     */
    public boolean shouldDeflect(int level) {
        return random.nextInt(100) < level * 12;
    }
}
