package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Immunity extends CustomEnchantment {

    public Immunity() {
        super("immunity", "Immunity", 4, EnchantTier.ULTIMATE, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "Grants a chance to completely negate incoming damage.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (level >= 2) player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, true, false));
        if (level >= 3) player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 100, 0, true, false));
        if (level >= 4) player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, true, false));
    }
}
