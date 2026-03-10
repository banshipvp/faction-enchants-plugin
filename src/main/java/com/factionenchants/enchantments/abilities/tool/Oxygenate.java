package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;

/**
 * Oxygenate – Pickaxe enchantment, ELITE tier.
 * Refills air supply while the player is underwater.
 */
public class Oxygenate extends CustomEnchantment {

    public Oxygenate() {
        super("oxygenate", "Oxygenate", 3, EnchantTier.ELITE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Refills your air supply while mining underwater.";
    }

    /**
     * Called by EnchantListener when the player is in water.
     */
    public void tryRefill(Player player) {
        if (player.getRemainingAir() < player.getMaximumAir()) {
            player.setRemainingAir(Math.min(player.getMaximumAir(),
                    player.getRemainingAir() + 30));
        }
    }
}
