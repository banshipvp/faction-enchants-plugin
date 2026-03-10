package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

/**
 * Leadership V — Armor enchantment (LEGENDARY).
 * The more allied players near you, the more damage you deal. Each nearby player
 * (other than the attacker and the target) within 20 blocks adds a damage bonus.
 * Bonus per ally: level * 0.08 (8%–40% per ally at max level, stacking linearly).
 * Maximum stacking: 5 allies for balance.
 */
public class Leadership extends CustomEnchantment {

    private static final double RADIUS = 20.0;
    private static final int MAX_ALLIES = 5;

    public Leadership() {
        super("leadership", "Leadership", 5, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "The more allies near you, the more damage you deal.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        List<Entity> nearby = attacker.getNearbyEntities(RADIUS, RADIUS, RADIUS);
        int allyCount = 0;
        for (Entity e : nearby) {
            if (!(e instanceof Player)) continue;
            if (e.equals(target)) continue;
            allyCount++;
            if (allyCount >= MAX_ALLIES) break;
        }
        if (allyCount == 0) return;

        double bonus = 1.0 + allyCount * level * 0.08;
        event.setDamage(event.getDamage() * bonus);
    }
}
