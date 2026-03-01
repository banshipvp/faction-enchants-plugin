package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GodlyOverload extends CustomEnchantment {

    public GodlyOverload() {
        super("godly_overload", "Godly Overload", 3, EnchantTier.HEROIC, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Extreme speed and damage boost.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, level + 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, level - 1, true, false));
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "overload";
    }
}
