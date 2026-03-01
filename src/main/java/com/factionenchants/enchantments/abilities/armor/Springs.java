package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Springs extends CustomEnchantment {

    public Springs() {
        super("springs", "Springs", 3, EnchantTier.ELITE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Gives jump boost.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, level - 1, true, false));
    }
}
