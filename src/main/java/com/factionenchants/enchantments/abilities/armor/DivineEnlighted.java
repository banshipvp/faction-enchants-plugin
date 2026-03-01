package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DivineEnlighted extends CustomEnchantment {

    public DivineEnlighted() {
        super("divine_enlighted", "Divine Enlighted", 5, EnchantTier.HEROIC, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Ultimate regeneration and absorption.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, level, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, level + 1, true, false));
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "enlighted";
    }
}
