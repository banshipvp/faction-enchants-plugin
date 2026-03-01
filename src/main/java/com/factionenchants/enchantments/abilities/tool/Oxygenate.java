package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Oxygenate extends CustomEnchantment {

    public Oxygenate() {
        super("oxygenate", "Oxygenate", 1, EnchantTier.SIMPLE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Refills oxygen levels when breaking blocks under water.";
    }

    /** Called from EnchantListener.onBlockBreak when player is underwater. */
    public void tryRefill(Player player) {
        if (player.getRemainingAir() < player.getMaximumAir()) {
            player.setRemainingAir(player.getMaximumAir());
        }
    }
}
