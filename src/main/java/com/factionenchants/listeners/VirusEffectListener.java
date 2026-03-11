package com.factionenchants.listeners;

import com.factionenchants.enchantments.abilities.bow.Virus;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

/**
 * Multiplies Wither and Poison damage for entities infected by the Virus enchantment.
 */
public class VirusEffectListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.WITHER
                && cause != EntityDamageEvent.DamageCause.POISON) return;

        UUID uid = entity.getUniqueId();
        long[] data = Virus.infected.get(uid);
        if (data == null) return;

        if (System.currentTimeMillis() > data[1]) {
            Virus.infected.remove(uid);
            return;
        }

        int level = (int) data[0];
        // +50% extra damage per level
        event.setDamage(event.getDamage() * (1.0 + level * 0.5));
    }
}
