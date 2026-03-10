package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Obsidianshield extends CustomEnchantment {

    public Obsidianshield() {
        super("obsidianshield", "Obsidianshield", 1, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Gives permanent fire resistance.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (!player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0, false, false, true));
        }
    }
}
