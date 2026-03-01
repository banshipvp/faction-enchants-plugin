package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Gears extends CustomEnchantment {

    public Gears() {
        super("gears", "Gears", 3, EnchantTier.LEGENDARY, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Added speed when equipped.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, level - 1, true, false));
    }
}
