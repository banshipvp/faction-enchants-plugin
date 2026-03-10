package com.factionenchants.enchantments.abilities.sword;

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
 * Silence IV — Sword enchantment (LEGENDARY).
 * On hit, there is a (level * 15)% chance to briefly silence the target,
 * preventing their defensive armor enchants from activating.
 * Duration: level * 2 seconds.
 *
 * The CombatListener/onHurtBy hooks check {@link Silence#isSilenced(UUID)}
 * before firing defensive enchant procs.
 */
public class Silence extends CustomEnchantment {

    /** Set of silenced player UUIDs. Their onHurtBy enchants are suppressed. */
    public static final Set<UUID> SILENCED = Collections.synchronizedSet(new HashSet<>());
    private static final Map<UUID, Integer> silenceTasks = new ConcurrentHashMap<>();

    public Silence() {
        super("silence", "Silence", 4, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Chance to stop activation of your enemy's custom defensive enchants for a brief period of time.";
    }

    public static boolean isSilenced(UUID playerId) {
        return SILENCED.contains(playerId);
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;

        double procChance = level * 0.15;
        if (Math.random() >= procChance) return;

        Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;

        UUID id = victim.getUniqueId();

        // Cancel any existing silence task
        Integer old = silenceTasks.remove(id);
        if (old != null) Bukkit.getScheduler().cancelTask(old);

        SILENCED.add(id);
        long durationTicks = (long) level * 40L; // level * 2 seconds
        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            SILENCED.remove(id);
            silenceTasks.remove(id);
        }, durationTicks);
        silenceTasks.put(id, taskId);
    }
}
