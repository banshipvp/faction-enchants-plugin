package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Reforged X — Sword enchantment, ELITE tier.
 * Protects tool durability, items will take longer to break.
 * Handled via EnchantListener's PlayerItemDamageEvent.
 */
public class Reforged extends CustomEnchantment {

    public Reforged() {
        super("reforged", "Reforged", 10, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Protects tool durability, items will take longer to break.";
    }

    /**
     * Reduces the durability damage by 8% per level (max 80% reduction at level X).
     */
    @Override
    public int onItemDamage(Player player, ItemStack item, int level, int originalDamage) {
        double reduction = 0.08 * level;
        return (int) Math.max(0, Math.round(originalDamage * (1.0 - reduction)));
    }
}
