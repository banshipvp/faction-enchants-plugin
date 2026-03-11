package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * BossSlayer V -- Axe Enchantment.
 * Deal an extra (level * 5%) base damage to bosses (Wither, Ender Dragon, Elder Guardian, Warden).
 */
public class BossSlayer extends CustomEnchantment {

    public BossSlayer() {
        super("boss_slayer", "Boss Slayer", 5, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Deal an extra (level x 5%) base damage to bosses.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!isBoss(target)) return;
        double bonus = level * 0.05;
        event.setDamage(event.getDamage() * (1.0 + bonus));
    }

    private boolean isBoss(LivingEntity entity) {
        return entity instanceof EnderDragon
                || entity instanceof Wither
                || entity instanceof ElderGuardian
                || entity instanceof Warden;
    }
}
