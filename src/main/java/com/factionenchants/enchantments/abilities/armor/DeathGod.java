package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Death God — Helmet enchantment.
 * When an attack would bring your HP to (level + 4) hearts or lower,
 * you have a chance to heal (level + 5) hearts instead of taking that damage.
 */
public class DeathGod extends CustomEnchantment {

    private final Random random = new Random();

    public DeathGod() {
        super("death_god", "Death God", 3, EnchantTier.LEGENDARY, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Attacks that bring your HP to (level+4) hearts or lower have a " +
               "chance to heal you for (level+5) hearts instead.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Threshold: (level + 4) hearts = (level + 4) * 2 HP on 20-HP scale
        double threshold = (level + 4) * 2.0;
        double hpAfter = defender.getHealth() - event.getFinalDamage();
        if (hpAfter > threshold) return;
        // 30% proc chance per level (30-90%)
        if (random.nextInt(100) >= level * 30) return;
        // Cancel the damage and instead heal (level + 5) hearts
        event.setCancelled(true);
        double heal = (level + 5) * 2.0;
        defender.setHealth(Math.min(defender.getMaxHealth(), defender.getHealth() + heal));
        defender.sendMessage("\u00a76Death God saved you!");
    }
}
