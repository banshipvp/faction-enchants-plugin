package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Devour — Axe enchantment.
 * Multiplies damage dealt to targets who currently have an active Bleed DoT.
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
        // Each level adds 25% bonus damage (up to 2x at level 4)
        double multiplier = 1.0 + level * 0.25;
        event.setDamage(event.getDamage() * multiplier);
    }
}
