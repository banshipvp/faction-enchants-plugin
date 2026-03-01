package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PolymorphicMetaphysical extends CustomEnchantment {

    public PolymorphicMetaphysical() {
        super("polymorphic_metaphysical", "Polymorphic Metaphysical", 4, EnchantTier.HEROIC, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Advanced immunity to slowness and other debuffs.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (player.hasPotionEffect(PotionEffectType.SLOW)) player.removePotionEffect(PotionEffectType.SLOW);
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) player.removePotionEffect(PotionEffectType.WEAKNESS);
        if (player.hasPotionEffect(PotionEffectType.POISON)) player.removePotionEffect(PotionEffectType.POISON);
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "metaphysical";
    }
}
