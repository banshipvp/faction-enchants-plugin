package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Devour IV — Axe enchantment (LEGENDARY).
 * Multiplies damage dealt to players that are currently suffering from a
 * Bleed stack (tracked by {@link Bleed#BLEED_VICTIMS}).
 * Damage multiplier: 1.0 + level * 0.25 (e.g., level 4 = 2.0× damage).
 */
public class Devour extends CustomEnchantment {

    public Devour() {
        super("devour", "Devour", 4, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Multiplies damage dealt to players with active bleed stacks from the Bleed enchantment.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!Bleed.BLEED_VICTIMS.contains(target.getUniqueId())) return;

        double multiplier = 1.0 + level * 0.25;
        event.setDamage(event.getDamage() * multiplier);
    }
}
