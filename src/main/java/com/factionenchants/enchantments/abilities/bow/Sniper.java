package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Sniper V — Bow enchantment (LEGENDARY).
 * Long-range arrow shots deal bonus damage. The farther the arrow traveled,
 * the more bonus damage is applied, up to 1.25× normal damage.
 * Minimum range for bonus: 15 blocks. Maximum bonus at 40+ blocks.
 */
public class Sniper extends CustomEnchantment {

    private static final double MIN_RANGE = 15.0;
    private static final double MAX_RANGE = 40.0;
    private static final double MAX_BONUS_MULT = 1.25;

    public Sniper() {
        super("sniper", "Sniper", 5, EnchantTier.LEGENDARY, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Headshots with projectile deal up to 1.25x damage.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Calculate arrow travel distance
        double distance = 0;
        if (event.getDamager() instanceof Arrow arrow) {
            distance = arrow.getLocation().distance(shooter.getLocation());
        }
        if (distance < MIN_RANGE) return;

        // Scale bonus from 1.0× at MIN_RANGE to MAX_BONUS_MULT at MAX_RANGE
        double fraction = Math.min((distance - MIN_RANGE) / (MAX_RANGE - MIN_RANGE), 1.0);
        // Scale further by level (level 1 = 20% of max bonus, level 5 = 100%)
        double levelScale = (double) level / getMaxLevel();
        double multiplier = 1.0 + fraction * (MAX_BONUS_MULT - 1.0) * levelScale;
        event.setDamage(event.getDamage() * multiplier);
    }
}
