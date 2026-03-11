package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

/**
 * Clarity – Armor enchantment, LEGENDARY tier, max level III.
 * Immune to Blindness up to the level of the clarity enchantment.
 */
public class Clarity extends CustomEnchantment {

    public Clarity() {
        super("clarity", "Clarity", 3, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Immune to Blindness up to level of clarity enchantment.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            var effect = player.getPotionEffect(PotionEffectType.BLINDNESS);
            if (effect != null && effect.getAmplifier() < level) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
            }
        }
    }
}
