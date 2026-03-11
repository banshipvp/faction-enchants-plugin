package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Execute VII — Sword enchantment, ELITE tier.
 * Damage buff when your target is at low hp.
 */
public class Execute extends CustomEnchantment {

    public Execute() {
        super("execute", "Execute", 7, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "5% damage bonus per level when attacking targets below 30% HP.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double maxHp = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double healthRatio = target.getHealth() / maxHp;

        // Activates when target is at or below 30% HP
        if (healthRatio <= 0.3) {
            double bonus = 1.0 + (level * 0.05); // 5% bonus damage per level
            event.setDamage(event.getDamage() * bonus);
        }
    }
}
