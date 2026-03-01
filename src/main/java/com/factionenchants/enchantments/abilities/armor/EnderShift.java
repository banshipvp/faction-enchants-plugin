package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnderShift extends CustomEnchantment {

    public EnderShift() {
        super("ender_shift", "EnderShift", 3, EnchantTier.UNIQUE, ApplicableGear.HELMET, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Gives speed/health boost at low HP.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (player.getHealth() < player.getMaxHealth() * 0.4) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, level, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 80, level - 1, true, false));
        }
    }
}
