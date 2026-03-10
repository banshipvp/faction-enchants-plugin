package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Rogue extends CustomEnchantment {

    public Rogue() {
        super("rogue", "Rogue", 3, EnchantTier.SOUL, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Active Soul Enchantment. Deal up to 2.0x damage if hitting your enemy from behind. Costs 100 souls per proc.";
    }

    /**
     * Soul cost per hit (proc) – consumed by CombatListener before calling onHitEntity.
     */
    @Override
    public int getSoulCostPerProc() {
        return 100;
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        float attackerYaw = normalizeYaw(attacker.getLocation().getYaw());
        float targetYaw   = normalizeYaw(target.getLocation().getYaw());

        // Angular difference between attacker's facing and target's facing.
        // If attacker is facing the same direction as target, attacker is behind.
        float diff = Math.abs(attackerYaw - targetYaw);
        if (diff > 180) diff = 360 - diff;

        // diff < 60 → clearly behind, interpolate multiplier up to 2.0x
        if (diff < 90) {
            // multiplier: 1.0 (head-on, diff=90) → 2.0 (directly behind, diff=0)
            double multiplier = 1.0 + (1.0 * (1.0 - diff / 90.0)) * level / 3.0;
            multiplier = Math.min(multiplier, 1.0 + (level / 3.0));
            event.setDamage(event.getDamage() * multiplier);
        }
    }

    private float normalizeYaw(float yaw) {
        yaw = yaw % 360;
        if (yaw < 0) yaw += 360;
        return yaw;
    }
}
