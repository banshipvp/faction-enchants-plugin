package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Barbarian – Axe enchantment, LEGENDARY tier, max level IV.
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
        if (!(target instanceof Player victim)) return;
        ItemStack held = victim.getInventory().getItemInMainHand();
        if (held == null) return;
        String type = held.getType().name();
        if (type.endsWith("_AXE")) {
            double multiplier = 1.0 + (level * 0.25); // +25% per level
            event.setDamage(event.getDamage() * multiplier);
        }
    }
}
