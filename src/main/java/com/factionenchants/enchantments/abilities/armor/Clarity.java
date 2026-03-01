package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Clarity extends CustomEnchantment {

    public Clarity() {
        super("clarity", "Clarity", 5, EnchantTier.LEGENDARY, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Passively removes negative potion effects.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) player.removePotionEffect(PotionEffectType.BLINDNESS);
        if (player.hasPotionEffect(PotionEffectType.CONFUSION)) player.removePotionEffect(PotionEffectType.CONFUSION);
        if (player.hasPotionEffect(PotionEffectType.SLOW) && level >= 3) player.removePotionEffect(PotionEffectType.SLOW);
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS) && level >= 4) player.removePotionEffect(PotionEffectType.WEAKNESS);
        if (player.hasPotionEffect(PotionEffectType.POISON) && level >= 5) player.removePotionEffect(PotionEffectType.POISON);
    }
}
