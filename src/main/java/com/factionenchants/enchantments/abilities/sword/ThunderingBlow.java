package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Thundering Blow III — Sword enchantment, UNIQUE tier.
 * Can cause smite (lightning strike) effect on your enemy.
 */
public class ThunderingBlow extends CustomEnchantment {

    private static final Random random = new Random();

    public ThunderingBlow() {
        super("thunderingblow", "Thundering Blow", 3, EnchantTier.UNIQUE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Can cause smite effect on your enemy.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 20% chance per level (20%, 40%, 60% at levels I–III)
        if (random.nextInt(100) < level * 20) {
            target.getWorld().strikeLightning(target.getLocation());
        }
    }
}
