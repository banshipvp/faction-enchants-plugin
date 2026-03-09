package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Barbarian — Axe enchantment.
 * Multiplies damage against players who are wielding an axe at the time they are hit.
 */
public class Barbarian extends CustomEnchantment {

    public Barbarian() {
        super("barbarian", "Barbarian", 4, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Multiplies damage against players who are wielding an AXE at the time they are hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player p)) return;
        ItemStack held = p.getInventory().getItemInMainHand();
        if (held == null) return;
        String typeName = held.getType().name();
        boolean holdsAxe = typeName.endsWith("_AXE");
        if (!holdsAxe) return;
        // Each level adds 15% bonus damage (up to 1.60x at level 4)
        double multiplier = 1.0 + level * 0.15;
        event.setDamage(event.getDamage() * multiplier);
    }
}
