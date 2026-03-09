package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Destruction — Helmet enchantment.
 * Every passive tick, automatically damages and debuffs all nearby enemies within 5 blocks.
 * Soul cost per tick prevents it from being free.
 */
public class Destruction extends CustomEnchantment {

    private final Random random = new Random();

    public Destruction() {
        super("destruction", "Destruction", 5, EnchantTier.LEGENDARY, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Automatically damages and debuffs all nearby enemies each second.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        for (Entity nearby : player.getNearbyEntities(5, 5, 5)) {
            if (!(nearby instanceof LivingEntity le)) continue;
            if (nearby instanceof Player np && np.equals(player)) continue;
            // Damage each nearby enemy
            le.damage(level * 0.5, player);
            // Apply random debuffs
            if (random.nextBoolean()) {
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, level - 1, true, false));
            }
            if (level >= 3 && random.nextBoolean()) {
                le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 0, true, false));
            }
        }
    }
}
