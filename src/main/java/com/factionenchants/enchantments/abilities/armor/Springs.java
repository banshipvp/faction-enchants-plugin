package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Springs III — Boots enchantment, ELITE tier.
 * Passively gives a jump boost while equipped.
 */
public class Springs extends CustomEnchantment {

    public Springs() {
        super("springs", "Springs", 3, EnchantTier.ELITE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Gives jump boost.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Keep jump boost refreshed; amplifier = level - 1 (Jump Boost I/II/III)
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, level - 1, false, false));
    }
}
