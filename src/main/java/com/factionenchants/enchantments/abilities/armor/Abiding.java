package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Abiding extends CustomEnchantment {

    public Abiding() {
        super("abiding", "Abiding", 5, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Grants permanent resistance while worn.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0, true, false));
    }
}
