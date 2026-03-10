package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Death God – Helmet enchantment, LEGENDARY tier, max level III.
 * Attacks that bring your HP to (level+4) hearts or lower have a chance
 * to heal you for (level+5) hearts instead.
 */
public class DeathGod extends CustomEnchantment {

    private static final Random random = new Random();

    public DeathGod() {
        super("death_god", "Death God", 3, EnchantTier.LEGENDARY, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Attacks that bring your HP to (level+4) hearts or lower have a chance to heal you for (level+5) hearts instead.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double hpAfter = defender.getHealth() - event.getFinalDamage();
        double threshold = (level + 4) * 2.0; // hearts → half-hearts
        if (hpAfter <= threshold && random.nextInt(100) < 30 + level * 10) {
            event.setCancelled(true);
            double heal = (level + 5) * 2.0;
            defender.setHealth(Math.min(defender.getMaxHealth(), defender.getHealth() + heal));
        }
    }
}
