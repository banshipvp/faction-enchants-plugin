package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Endershift III — Helmet enchantment, UNIQUE tier.
 * Grants a burst of Speed and a health boost when the wearer drops below 30% HP.
 */
public class Endershift extends CustomEnchantment {

    public Endershift() {
        super("ender_shift", "Endershift", 3, EnchantTier.UNIQUE, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Gives a speed and health boost when you drop below 30% HP.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        double hpPercent = player.getHealth() / player.getMaxHealth();
        if (hpPercent <= 0.30) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SPEED, 60, level - 1, false, false));
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.HEALTH_BOOST, 60, level - 1, false, false));
        }
    }
}
