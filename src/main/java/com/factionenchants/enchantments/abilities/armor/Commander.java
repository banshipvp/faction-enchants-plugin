package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Commander extends CustomEnchantment {

    public Commander() {
        super("commander", "Commander", 5, EnchantTier.UNIQUE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Nearby allies are given haste.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        int radius = 6 + level * 2;
        for (org.bukkit.entity.Entity e : player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius)) {
            if (!(e instanceof Player ally)) continue;
            if (ally.equals(player)) continue;
            ally.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60, level - 1, true, false));
        }
    }
}
