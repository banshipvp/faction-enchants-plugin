package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Enlighted — Armor enchantment.
 * Can heal hearts even while actively taking damage. Procs on each hit received.
 */
public class Enlighted extends CustomEnchantment {

    private final Random random = new Random();

    public Enlighted() {
        super("enlighted", "Enlighted", 3, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "You can heal hearts even while taking damage. Each hit received has a chance to heal you.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Proc chance: 25% per level (25–75%)
        if (random.nextInt(100) >= level * 25) return;
        double heal = level * 1.0;
        defender.setHealth(Math.min(defender.getMaxHealth(), defender.getHealth() + heal));
    }
}
