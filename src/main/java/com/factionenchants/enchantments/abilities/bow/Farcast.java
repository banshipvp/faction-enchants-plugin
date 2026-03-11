package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Farcast V — Bow enchantment, ELITE tier.
 * Chance to knockback melee attackers by a couple of blocks when they hit you.
 * The lower your health, the higher the chance to proc.
 */
public class Farcast extends CustomEnchantment {

    private static final Random random = new Random();

    public Farcast() {
        super("farcast", "Farcast", 5, EnchantTier.ELITE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Chance to knockback melee attackers. Lower health increases proc chance.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Only proc on melee hits (i.e. direct entity attacker, not arrows)
        if (!(attacker instanceof Player)) return;

        double maxHp = defender.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double healthRatio = defender.getHealth() / maxHp;

        // Base chance: level * 5%; bonus scales with missing health (up to level * 10% extra)
        double chance = (level * 0.05) + (1.0 - healthRatio) * (level * 0.10);

        if (random.nextDouble() < chance) {
            Vector direction = attacker.getLocation().toVector()
                    .subtract(defender.getLocation().toVector())
                    .normalize()
                    .multiply(2.5);
            direction.setY(0.4);
            attacker.setVelocity(direction);
        }
    }
}
