package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Sniper V — Bow enchantment (LEGENDARY).
 * Headshots (arrows that hit at or above the target's eye level) deal bonus damage.
 * Damage multiplier scales with level: up to 1.25× at max level.
 */
public class Sniper extends CustomEnchantment {

    private static final double MAX_BONUS_MULT = 1.25;

    public Sniper() {
        super("sniper", "Sniper", 5, EnchantTier.LEGENDARY, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Headshots with projectiles deal up to 1.25x damage (scales with level).";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow arrow)) return;

        // Check if arrow hit at or above eye level (headshot detection)
        double arrowY = arrow.getLocation().getY();
        double targetEyeY = target.getEyeLocation().getY();
        double targetFeetY = target.getLocation().getY();
        
        // Headshot zone: upper 30% of the entity's height (around head/upper torso)
        double entityHeight = targetEyeY - targetFeetY;
        double headshotThreshold = targetFeetY + (entityHeight * 0.7); // Top 30% of body

        if (arrowY < headshotThreshold) return; // Not a headshot

        // Headshot confirmed - apply bonus damage scaled by level
        // Level 1 = 20% of max bonus, Level 5 = 100% of max bonus
        double levelScale = (double) level / getMaxLevel();
        double multiplier = 1.0 + (MAX_BONUS_MULT - 1.0) * levelScale;
        event.setDamage(event.getDamage() * multiplier);
        
        shooter.sendMessage("§c§l✦ HEADSHOT!");
    }
}
