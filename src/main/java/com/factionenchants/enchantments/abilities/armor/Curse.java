package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Curse extends CustomEnchantment {

    public Curse() {
        super("curse", "Curse", 5, EnchantTier.UNIQUE, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "Gives strength, slowness and resistance at low HP.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (player.getHealth() < player.getMaxHealth() * 0.35) {
            int dur = 80;
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, dur, level - 1, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, dur, level - 1, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, dur, level - 1, true, false));
        }
    }
}
