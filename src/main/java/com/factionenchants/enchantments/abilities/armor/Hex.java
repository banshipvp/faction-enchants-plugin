package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hex — Axe enchantment.
 * Once a target is afflicted with Hex, a portion of all their outgoing damage
 * is reflected back onto them for up to 4 seconds.
 * Reflection is checked in CombatListener when the hexed player attacks.
 */
public class Hex extends CustomEnchantment {

    /** Maps hexed player UUID → expiry timestamp (ms). */
    public static final Map<UUID, Long> HEXED = new ConcurrentHashMap<>();
    /** Reflection ratio: 20% per level. */
    private static final double REFLECT_PER_LEVEL = 0.20;

    private final Random random = new Random();

    public Hex() {
        super("hex", "Hex", 5, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Afflicts the target with Hex for up to 4 seconds. While hexed, a portion of their outgoing damage is reflected back onto them.";
    }

    public static boolean isHexed(UUID id) {
        Long expiry = HEXED.get(id);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) { HEXED.remove(id); return false; }
        return true;
    }

    /**
     * Returns the reflection ratio for a hexed player (0.0 if not hexed).
     * Called from CombatListener when a player attacks someone.
     */
    public static double getReflectRatio(UUID id, int level) {
        return isHexed(id) ? Math.min(level * REFLECT_PER_LEVEL, 1.0) : 0.0;
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Apply Hex debuff to the target: random proc 60% + level*8%
        if (random.nextInt(100) >= 60 + level * 8) return;
        HEXED.put(target.getUniqueId(), System.currentTimeMillis() + 4000L);
        if (target instanceof Player p) {
            p.sendMessage("\u00a75You have been Hexed!");
        }
    }
}
