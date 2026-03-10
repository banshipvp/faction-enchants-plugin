package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hex V — Axe enchantment (LEGENDARY).
 * On hit, afflicts the target with Hex for 4 seconds. While hexed, a portion of
 * all damage they deal is reflected back onto them.
 *
 * CombatListener checks {@link Hex#getReflectionFraction(UUID)} for hexed attackers
 * and applies the reflected damage after the normal hit processing.
 *
 * Reflect fraction: level * 0.12 (12%–60% at max).
 */
public class Hex extends CustomEnchantment {

    /** Maps hexed player UUID -> reflection fraction (0.0–1.0). */
    public static final Map<UUID, Double> HEX_VICTIMS = new ConcurrentHashMap<>();

    /** Hex duration: 4 seconds (80 ticks). */
    private static final long HEX_DURATION_TICKS = 80L;

    public Hex() {
        super("hex", "Hex", 5, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Once a target is afflicted with Hex, a portion of all their outgoing damage is reflected back onto them for up to 4 seconds.";
    }

    /**
     * Returns the reflection fraction for the given player, or 0 if not hexed.
     */
    public static double getReflectionFraction(UUID playerId) {
        return HEX_VICTIMS.getOrDefault(playerId, 0.0);
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        UUID targetId = target.getUniqueId();
        double fraction = level * 0.12;
        HEX_VICTIMS.put(targetId, fraction);

        Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> HEX_VICTIMS.remove(targetId), HEX_DURATION_TICKS);
    }
}
