package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Deathbringer III — Armor Enchantment.
 * Chance to deal double damage on attack.
 * Proc is checked via the attacker's armor in CombatListener.
 */
public class Deathbringer extends CustomEnchantment {

    private static final Random random = new Random();

    public Deathbringer() {
        super("deathbringer", "Deathbringer", 3, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "15% chance per level to deal double damage (max 45%).";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Proc chance: 15% per level (15%, 30%, 45%)
        if (random.nextInt(100) < level * 15) {
            event.setDamage(event.getDamage() * 2.0);
        }
    }
}
