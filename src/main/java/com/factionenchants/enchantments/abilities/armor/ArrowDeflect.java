package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Arrow Deflect — Armor enchantment, ULTIMATE tier, max level IV.
 * Prevents you from being damaged by enemy arrows more often than once
 * every (level × 400) milliseconds.
 *
 * At level 1 you can only be arrow-damaged once per 400 ms.
 * At level 4 you can only be arrow-damaged once per 1600 ms.
 */
public class ArrowDeflect extends CustomEnchantment {

    /** Cooldown per level in milliseconds. */
    public static final long COOLDOWN_PER_LEVEL_MS = 400L;

    /** Maps player UUID → timestamp (ms) when arrow damage was last allowed through. */
    private static final Map<UUID, Long> lastHitTime = new ConcurrentHashMap<>();

    public ArrowDeflect() {
        super("arrow_deflect", "Arrow Deflect", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Prevents you from being damaged by enemy arrows more often than once every level x 400 milliseconds.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Only activates against arrow projectiles
        if (!(attacker instanceof Arrow)) return;

        UUID id = defender.getUniqueId();
        long now = System.currentTimeMillis();
        long cooldownMs = level * COOLDOWN_PER_LEVEL_MS;
        Long last = lastHitTime.get(id);

        if (last != null && now - last < cooldownMs) {
            // Still within cooldown — cancel the damage
            event.setCancelled(true);
        } else {
            // Allow the hit and update the timestamp
            lastHitTime.put(id, now);
        }
    }
}
