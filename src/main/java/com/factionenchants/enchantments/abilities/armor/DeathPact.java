package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeathPact extends CustomEnchantment {

    public DeathPact() {
        super("death_pact", "Death Pact", 5, EnchantTier.MASTERY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Sacrifices health for ultimate power.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (player.getHealth() > 2) {
            player.setHealth(player.getHealth() - 0.5);
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INCREASE_DAMAGE, 60, level + 1, true, false));
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 60, level, true, false));
        }
    }
}
