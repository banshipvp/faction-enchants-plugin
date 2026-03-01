package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Implants extends CustomEnchantment {

    public Implants() {
        super("implants", "Implants", 3, EnchantTier.ULTIMATE, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Passively heals 1 health and restores 1 hunger every few seconds.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, level - 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 60, 0, true, false));
    }
}
