package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Insanity — Axe enchantment.
 * Multiplies damage against players who are wielding a sword at the time they are hit.
 */
public class Insanity extends CustomEnchantment {

    public Insanity() {
        super("insanity", "Insanity", 8, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "You swing your axe like a maniac. Multiplies damage against players who are wielding a sword at the time they are hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player p)) return;
        ItemStack held = p.getInventory().getItemInMainHand();
        if (held == null || !held.getType().name().endsWith("_SWORD")) return;
        // Each level adds 8% bonus damage (up to 1.64x at level 8)
        event.setDamage(event.getDamage() * (1.0 + level * 0.08));
    }
}
