package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Headless III — Sword enchantment, SIMPLE tier.
 * Victims have a chance of dropping their head on death.
 */
public class Headless extends CustomEnchantment {

    private static final Random random = new Random();

    public Headless() {
        super("headless", "Headless", 3, EnchantTier.SIMPLE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Victims have a chance of dropping their head on death.";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        // 15% chance per level (15%, 30%, 45%)
        if (random.nextInt(100) < level * 15) {
            if (victim instanceof Player targetPlayer) {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                org.bukkit.inventory.meta.SkullMeta meta = (org.bukkit.inventory.meta.SkullMeta) head.getItemMeta();
                meta.setOwningPlayer(targetPlayer);
                head.setItemMeta(meta);
                victim.getWorld().dropItemNaturally(victim.getLocation(), head);
            }
        }
    }
}
