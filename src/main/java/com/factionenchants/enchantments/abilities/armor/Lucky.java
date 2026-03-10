package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Lucky extends CustomEnchantment {

    private final Random random = new Random();

    public Lucky() {
        super("lucky", "Lucky", 10, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "You will find yourself more lucky in all Cosmic situations.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Apply Luck potion effect - amplifier scales with level
        int amplifier = Math.min(level - 1, 9);
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 40, amplifier, false, false, true));
    }
}
