package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Aegis — Armor enchantment.
 * If the wearer is hit by more than (8 - level) different enemies within 5 seconds,
 * damage from any additional attacker is halved.
 */
public class Aegis extends CustomEnchantment {

    /** Maps defender UUID -> (attacker UUID -> last hit timestamp ms). */
    public static final Map<UUID, Map<UUID, Long>> recentAttackers = new ConcurrentHashMap<>();
    private static final long WINDOW_MS = 5000L;

    public Aegis() {
        super("aegis", "Aegis", 6, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "If you are taking damage from more than (8-level) enemies in a short period, "
             + "the damage from any additional players beyond that initial group will be halved.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        UUID defId = defender.getUniqueId();
        UUID atkId = attacker.getUniqueId();
        long now = System.currentTimeMillis();

        Map<UUID, Long> attackers = recentAttackers.computeIfAbsent(defId, k -> new ConcurrentHashMap<>());
        // Purge stale entries
        attackers.entrySet().removeIf(e -> now - e.getValue() > WINDOW_MS);
        attackers.put(atkId, now);

        // Threshold: at level 1 you need >7 diff attackers, at level 6 you need >2
        int threshold = 8 - level;
        if (attackers.size() > threshold) {
            event.setDamage(event.getDamage() * 0.5);
        }
    }
}
