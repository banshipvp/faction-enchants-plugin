package com.factionenchants.enchantments.abilities.misc;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Quick Reeler V — Fishing Rod enchantment, ELITE tier.
 * Reduces the time it takes for fish to bite by up to 25%.
 */
public class QuickReeler extends CustomEnchantment {

    public QuickReeler() {
        super("quickreeler", "Quick Reeler", 5, EnchantTier.ELITE, ApplicableGear.FISHING_ROD);
    }

    @Override
    public String getDescription() {
        return "Reduces the time it takes for fish to bite by up to 25%.";
    }

    @Override
    public void onFishCast(Player player, int level, PlayerFishEvent event) {
        FishHook hook = event.getHook();
        // Reduce wait times by 5% per level (25% max at level V)
        double reduction = 1.0 - (level * 0.05);
        int minWait = (int) (hook.getMinWaitTime() * reduction);
        int maxWait = (int) (hook.getMaxWaitTime() * reduction);
        hook.setMinWaitTime(Math.max(1, minWait));
        hook.setMaxWaitTime(Math.max(1, maxWait));
    }
}
