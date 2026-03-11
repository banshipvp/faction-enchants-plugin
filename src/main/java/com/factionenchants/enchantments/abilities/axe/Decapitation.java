package com.factionenchants.enchantments.abilities.axe;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

/**
 * Decapitation III — Axe enchantment, SIMPLE tier.
 * Victims have a chance of dropping their head on death.
 */
public class Decapitation extends CustomEnchantment {

    private static final Random random = new Random();

    public Decapitation() {
        super("decapitation", "Decapitation", 3, EnchantTier.SIMPLE, ApplicableGear.AXE);
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
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwningPlayer(targetPlayer);
                head.setItemMeta(meta);
                victim.getWorld().dropItemNaturally(victim.getLocation(), head);
            }
        }
    }
}
