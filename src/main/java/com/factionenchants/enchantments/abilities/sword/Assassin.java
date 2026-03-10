package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Assassin — Sword enchantment, ULTIMATE tier, max level V.
 * The closer you are to your enemy, the more damage you deal (up to 1.25x).
 * However, if you are more than 3 blocks away, you will deal LESS damage.
 *
 * Formula (per level):
 *   distance ≤ 1 block  → ×1.25 (full bonus)
 *   1–3 blocks          → linear scale from ×1.25 down to ×1.0
 *   > 3 blocks          → ×(1.0 - (distance - 3) * 0.03 * level), min ×0.70
 *
 * At higher levels the falloff beyond 3 blocks is harsher.
 */
public class Assassin extends CustomEnchantment {

    /** Maximum damage multiplier when right next to the target. */
    private static final double MAX_BONUS = 1.25;
    /** Blocks within which you get full or partial bonus. */
    private static final double BONUS_RANGE = 3.0;
    /** Minimum multiplier (at extreme range). */
    private static final double MIN_MULTIPLIER = 0.70;

    public Assassin() {
        super("assassin", "Assassin", 5, EnchantTier.ULTIMATE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "The closer you are to your enemy, the more damage you deal (up to 1.25x). " +
               "However, if you are more than 3 blocks away, you will deal LESS damage than normal.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double distance = attacker.getLocation().distance(target.getLocation());
        double multiplier;

        if (distance <= 1.0) {
            // Right next to target — full bonus
            multiplier = MAX_BONUS;
        } else if (distance <= BONUS_RANGE) {
            // Partial bonus: linearly scale from MAX_BONUS at 1 block → 1.0 at 3 blocks
            double t = (distance - 1.0) / (BONUS_RANGE - 1.0); // 0→1
            multiplier = MAX_BONUS - t * (MAX_BONUS - 1.0);
        } else {
            // Penalty beyond 3 blocks; harsher at higher levels
            double penalty = (distance - BONUS_RANGE) * 0.03 * level;
            multiplier = Math.max(MIN_MULTIPLIER, 1.0 - penalty);
        }

        event.setDamage(event.getDamage() * multiplier);
    }
}
