package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnderWalker extends CustomEnchantment {

    public EnderWalker() {
        super("ender_walker", "Ender Walker", 5, EnchantTier.ULTIMATE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "When Wither or Poison would damage you, gain regen instead.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (player.hasPotionEffect(PotionEffectType.WITHER) || player.hasPotionEffect(PotionEffectType.POISON)) {
            player.removePotionEffect(PotionEffectType.WITHER);
            player.removePotionEffect(PotionEffectType.POISON);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, level - 1, true, false));
        }
    }
}
