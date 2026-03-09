package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Adrenaline extends CustomEnchantment {

    public Adrenaline() {
        super("adrenaline", "Adrenaline", 4, EnchantTier.LEGENDARY, ApplicableGear.LEGGINGS, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Grants speed and strength when your health\ndrops below 50%.";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (player.getHealth() < player.getMaxHealth() * 0.3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, level, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, level - 1, true, false));
        }
    }
}
