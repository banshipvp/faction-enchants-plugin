package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GodlyOverload extends CustomEnchantment {

    public GodlyOverload() {
        super("godly_overload", "Godly Overload", 3, EnchantTier.HEROIC, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "Adds extra hearts on top of Overload plus combat boosts.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Godly Overload I = Overload 4 (amplifier 3, 18 hearts)
        // Godly Overload II = Overload 5 (amplifier 4, 20 hearts)
        // Godly Overload III = Overload 6 (amplifier 5, 22 hearts)
        int amplifier = level + 2;

        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, amplifier, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, level + 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, level - 1, true, false));
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "overload";
    }
}
