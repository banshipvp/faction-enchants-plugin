package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Shiva V — Weapon enchantment (LEGENDARY).
 * Deals extra bonus damage to all players.
 * Bonus damage: level * 0.5 HP per hit (stacks additively with other bonuses).
 */
public class Shiva extends CustomEnchantment {

    public Shiva() {
        super("shiva", "Shiva", 5, EnchantTier.LEGENDARY, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Deal extra damage to players.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player)) return;
        event.setDamage(event.getDamage() + level * 0.5);
    }
}
