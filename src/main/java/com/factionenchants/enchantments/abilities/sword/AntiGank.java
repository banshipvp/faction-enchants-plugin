package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * AntiGank – Sword enchantment, LEGENDARY tier.
 * Deals bonus damage for each nearby enemy player beyond the first.
 */
public class AntiGank extends CustomEnchantment {

    public AntiGank() {
        super("anti_gank", "Anti Gank", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Deal extra damage for each nearby enemy (level × 0.5 damage per enemy, 10 block radius).";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        long nearbyEnemies = target.getNearbyEntities(10, 10, 10).stream()
                .filter(e -> e instanceof Player && !e.equals(attacker))
                .count();
        if (nearbyEnemies > 0) {
            double bonus = nearbyEnemies * level * 0.5;
            event.setDamage(event.getDamage() + bonus);
        }
    }
}
