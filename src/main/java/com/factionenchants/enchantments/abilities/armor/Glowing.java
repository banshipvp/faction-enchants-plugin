package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Glowing extends CustomEnchantment {

    public Glowing() {
        super("glowing", "Glowing", 1, EnchantTier.SIMPLE, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Gives permanent night vision.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 400, 0, true, false));
    }
}
