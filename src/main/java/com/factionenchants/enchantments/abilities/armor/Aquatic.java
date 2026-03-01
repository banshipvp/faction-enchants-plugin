package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Aquatic extends CustomEnchantment {

    public Aquatic() {
        super("aquatic", "Aquatic", 1, EnchantTier.SIMPLE, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Gives permanent water breathing.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0, true, false));
    }
}
