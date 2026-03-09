package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Drunk extends CustomEnchantment {

    public Drunk() {
        super("drunk", "Drunk", 4, EnchantTier.LEGENDARY, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Grants strength at the cost of causing slowness and mining fatigue.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Strength scales with level; slowness and mining fatigue also scale with level
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, level - 1, true, false), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, level - 1, true, false), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, level - 1, true, false), true);
    }
}
