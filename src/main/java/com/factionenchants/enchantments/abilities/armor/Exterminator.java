package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Exterminator III — Leggings enchantment (LEGENDARY).
 * On hit, temporarily disables the target's Undead Fuse, Guardians, and Spirits
 * abilities. Suppression duration: (level * 3) seconds.
 *
 * Other ability classes (Fuse, Guardians, etc.) should check
 * {@link Exterminator#isSuppressed(UUID)} before activating their effects.
 */
public class Exterminator extends CustomEnchantment {

    /** Set of player UUIDs whose special abilities are currently suppressed. */
    public static final Set<UUID> SUPPRESSED = Collections.synchronizedSet(new HashSet<>());

    /** Tracks scheduled task IDs so we can cancel/replace them. */
    private static final Map<UUID, Integer> suppressTasks = new ConcurrentHashMap<>();

    public Exterminator() {
        super("exterminator", "Exterminator", 3, EnchantTier.LEGENDARY, ApplicableGear.LEGGINGS);
    }

    @Override
    public String getDescription() {
        return "Temporarily disables enemy ability to use Undead Fuse, Guardians, and Spirits.";
    }

    /**
     * Convenience check used by Fuse, Guardians, and Spirits to gate their effects.
     */
    public static boolean isSuppressed(UUID playerId) {
        return SUPPRESSED.contains(playerId);
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;

        UUID id = victim.getUniqueId();
        Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;

        // Cancel any existing suppression task for this target
        Integer oldTask = suppressTasks.remove(id);
        if (oldTask != null) {
            Bukkit.getScheduler().cancelTask(oldTask);
        }

        SUPPRESSED.add(id);

        long durationTicks = (long) level * 60L; // level * 3 seconds (20 ticks/s)
        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            SUPPRESSED.remove(id);
            suppressTasks.remove(id);
        }, durationTicks);
        suppressTasks.put(id, taskId);
    }
}
