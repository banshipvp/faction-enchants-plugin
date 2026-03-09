package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Diminish — Chestplate enchantment.
 * When this effect procs, the next attack dealt to you cannot deal more than
 * half the damage you took from the previous attack.
 */
public class Diminish extends CustomEnchantment {

    // Maps defender UUID → last raw damage they took
    private static final Map<UUID, Double> lastDamage = new HashMap<>();
    private final Random random = new Random();

    public Diminish() {
        super("diminish", "Diminish", 6, EnchantTier.ULTIMATE, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "When this effect procs, the next attack dealt to you cannot deal more "
             + "than the (total amount of damage / 2) you took from the previous attack. "
             + "(i.e. if you're damaged for 2HP, the next attack cannot deal more than 1HP of damage.)";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        UUID id = defender.getUniqueId();
        // Proc chance scales with level (15% per level → 90% at level 6)
        if (random.nextInt(100) >= level * 15) {
            // Still record last damage for next successful proc
            lastDamage.put(id, event.getFinalDamage());
            return;
        }

        double last = lastDamage.getOrDefault(id, Double.MAX_VALUE);
        double cap = last / 2.0;
        double incoming = event.getDamage();

        if (incoming > cap) {
            event.setDamage(cap);
        }
        // Record the (possibly capped) damage for next proc
        lastDamage.put(id, Math.min(incoming, cap));
    }
}
