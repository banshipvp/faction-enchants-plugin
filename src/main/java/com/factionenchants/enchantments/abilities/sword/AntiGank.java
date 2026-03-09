package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AntiGank — Axe enchantment.
 * Tracks how many different players have attacked the bearer recently.
 * If that count exceeds (6 - level), the bearer's outgoing damage is multiplied.
 */
public class AntiGank extends CustomEnchantment {

    /** Maps bearer UUID -> (attacker UUID -> last hit timestamp ms). */
    public static final Map<UUID, Map<UUID, Long>> recentHitsOnPlayer = new ConcurrentHashMap<>();
    private static final long WINDOW_MS = 6000L;

    public AntiGank() {
        super("antigank", "Anti Gank", 4, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "If multiple enemies have attacked you recently, your outgoing damage is multiplied "
             + "depending on how many unique attackers have hit you in the last 6 seconds.";
    }

    /**
     * Called by CombatListener whenever any player is attacked by another player.
     * Records the attacker so that when the victim later hits someone, AntiGank can proc.
     */
    public static void recordHit(UUID victim, UUID attacker) {
        recentHitsOnPlayer
            .computeIfAbsent(victim, k -> new ConcurrentHashMap<>())
            .put(attacker, System.currentTimeMillis());
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        UUID atkId = attacker.getUniqueId();
        long now = System.currentTimeMillis();
        Map<UUID, Long> attackers = recentHitsOnPlayer.getOrDefault(atkId, Map.of());
        // Purge stale
        attackers.entrySet().removeIf(e -> now - e.getValue() > WINDOW_MS);

        // Threshold: need more than (6-level) unique attackers to trigger
        int threshold = 6 - level;
        if (attackers.size() <= threshold) return;

        // Scale multiplier: each attacker beyond threshold adds 10%, capped at 50% extra
        int excess = Math.min(attackers.size() - threshold, 5);
        double multiplier = 1.0 + excess * 0.10;
        event.setDamage(event.getDamage() * multiplier);
    }
}
