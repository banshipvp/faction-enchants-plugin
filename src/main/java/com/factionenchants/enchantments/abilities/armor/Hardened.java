package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Hardened III — Armor enchantment, ELITE tier.
 * Armor takes less durability damage.
 */
public class Hardened extends CustomEnchantment {

    public Hardened() {
        super("hardened", "Hardened", 3, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Armor takes less durability damage.";
    }

    /**
     * Reduces durability damage by 25% per level (capped at 75% reduction at level III).
     * Called from EnchantListener's PlayerItemDamageEvent handler.
     */
    @Override
    public int onItemDamage(Player player, ItemStack item, int level, int originalDamage) {
        double reduction = 0.25 * level; // 25% per level
        return (int) Math.max(0, Math.round(originalDamage * (1.0 - reduction)));
    }
}
