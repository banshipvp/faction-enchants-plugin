package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Anti Gravity III — Boots enchantment, ELITE tier.
 * Gives the player a super jump but does not negate fall damage.
 */
public class AntiGravity extends CustomEnchantment {

    public AntiGravity() {
        super("antigravity", "Anti Gravity", 3, EnchantTier.ELITE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Super jump but does not negate fall damage.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Amplifier level - 1: level 1 = Jump Boost I, level 3 = Jump Boost III
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, level, false, false));
    }
}
