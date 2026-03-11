package com.factionenchants.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks mobs spawned from mob spawners for use by the Shackle enchantment.
 */
public class SpawnerMobTracker implements Listener {

    private static final Set<UUID> spawnerMobs =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static boolean isSpawnerMob(UUID uuid) {
        return spawnerMobs.contains(uuid);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            spawnerMobs.add(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        spawnerMobs.remove(event.getEntity().getUniqueId());
    }
}
