package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Armored — Armor enchantment.
 * Decreases damage from enemy swords by 1.85% per level. Stackable across pieces.
 */
public class Armored extends CustomEnchantment {

    public Armored() {
        super("armored", "Armored", 4, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Decreases damage from enemy swords by 1.85% per level, this enchantment is stackable.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Only triggers against players holding a sword
        if (!(attacker instanceof Player attackerPlayer)) return;
        ItemStack held = attackerPlayer.getInventory().getItemInMainHand();
        if (held == null || !held.getType().name().endsWith("_SWORD")) return;

        // 1.85% reduction per level, applied per armour piece (stacks additively via multiple calls)
        double reduction = level * 0.0185;
        event.setDamage(event.getDamage() * (1.0 - Math.min(reduction, 0.90)));
    }
}
