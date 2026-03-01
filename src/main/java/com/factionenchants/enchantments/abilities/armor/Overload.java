package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Overload extends CustomEnchantment {

    public Overload() {
        super("overload", "Overload", 3, EnchantTier.LEGENDARY,
                ApplicableGear.HELMET, ApplicableGear.CHESTPLATE, ApplicableGear.LEGGINGS, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Temporary increase in hearts while worn.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Use Health Boost potion effect instead of permanent attribute change
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, level - 1, true, false));
    }
}
