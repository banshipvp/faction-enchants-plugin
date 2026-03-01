package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Enlighted extends CustomEnchantment {

    public Enlighted() {
        super("enlighted", "Enlighted", 5, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Passively provides regeneration and absorption.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0, true, false));
        if (level >= 3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, level - 3, true, false));
        }
    }
}
