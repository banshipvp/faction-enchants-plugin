package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Diminish VI — Chestplate enchantment (LEGENDARY).
 * Tracks incoming damage. When the enchant procs, the very next attack received
 * cannot deal more than (last recorded damage / 2). Proc chance: level * 10%.
 */
public class Diminish extends CustomEnchantment {

    /** Maps player UUID -> last damage amount received (the "reference" damage). */
    private static final Map<UUID, Double> lastDamage = new ConcurrentHashMap<>();
    /** If true, the NEXT hit will be capped. Maps player UUID -> capped-hit-pending flag. */
    private static final Map<UUID, Boolean> capPending = new ConcurrentHashMap<>();

    public Diminish() {
        super("diminish", "Diminish", 6, EnchantTier.LEGENDARY, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "When this effect procs, the next attack dealt to you cannot deal more than "
             + "the (total amount of damage / 2) you took from the previous attack.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        UUID id = defender.getUniqueId();

        // If a cap is pending, apply it and clear the flag
        if (Boolean.TRUE.equals(capPending.get(id))) {
            capPending.remove(id);
            double prev = lastDamage.getOrDefault(id, Double.MAX_VALUE);
            double cap = prev / 2.0;
            if (event.getDamage() > cap) {
                event.setDamage(cap);
            }
        }

        // Record raw damage for next proc calculation, then roll for proc
        lastDamage.put(id, event.getDamage());

        double procChance = level * 0.10; // 10% per level
        if (Math.random() < procChance) {
            capPending.put(id, true);
        }
    }
}
