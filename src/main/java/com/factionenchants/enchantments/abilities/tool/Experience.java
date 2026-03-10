package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Experience – Tool enchantment, UNIQUE tier.
 * Chance to grant bonus XP when mining blocks.
 */
public class Experience extends CustomEnchantment {

    private static final Random random = new Random();

    public Experience() {
        super("experience", "Experience", 5, EnchantTier.UNIQUE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Chance to grant bonus XP when mining blocks.";
    }

    /**
     * Called by EnchantListener on block break.
     */
    public void tryGiveExp(Player player, int level) {
        if (random.nextInt(100) < level * 15) {
            player.giveExp(level * 2);
        }
    }
}
