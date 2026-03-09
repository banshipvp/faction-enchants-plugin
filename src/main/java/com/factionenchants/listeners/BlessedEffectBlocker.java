package com.factionenchants.listeners;

import com.factionenchants.commands.BlessCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Cancels Slowness and Mining Fatigue being applied to blessed players.
 * Players are added to the blessed set by {@link BlessCommand}.
 * Protection lasts until the player relogs.
 */
public class BlessedEffectBlocker implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!BlessCommand.BLESSED.contains(player.getUniqueId())) return;
        if (event.getAction() != EntityPotionEffectEvent.Action.ADDED
                && event.getAction() != EntityPotionEffectEvent.Action.CHANGED) return;

        if (event.getNewEffect() == null) return;
        PotionEffectType type = event.getNewEffect().getType();

        // Compare by internal key name — works across Spigot 1.20 and Paper 1.21
        String key = type.getKey().getKey(); // e.g. "slowness" or "mining_fatigue"
        if (key.equals("slowness") || key.equals("mining_fatigue")
                || key.equals("slow") || key.equals("slow_digging")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uid = event.getPlayer().getUniqueId();
        BlessCommand.BLESSED.remove(uid);
        BlessCommand.WAS_BLESSED.remove(uid);
    }
}
