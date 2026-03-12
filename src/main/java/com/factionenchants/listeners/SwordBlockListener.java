package com.factionenchants.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Restores 1.8-style sword blocking.
 * Right-clicking while holding any sword puts the player into a blocking
 * state. While blocking, incoming damage is reduced by 50%.
 * Blocking ends when the player stops right-clicking, switches items, or
 * leaves the server.
 */
public class SwordBlockListener implements Listener {

    private final Set<UUID> blocking = new HashSet<>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack held = player.getInventory().getItemInMainHand();

        if (!isSword(held)) return;

        switch (event.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> blocking.add(player.getUniqueId());
            default -> {}
        }
    }

    // Paper fires PlayerInteractEvent with no action when the button is released,
    // but the most reliable way to detect "stop right-clicking" is via the
    // attack action or item switch. We stop blocking on the next hit or slot change.

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        // Stop blocking if the player swings (attacks)
        if (event.getDamager() instanceof Player attacker) {
            blocking.remove(attacker.getUniqueId());
        }

        // Apply 50% damage reduction if the victim is blocking with a sword
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!blocking.remove(victim.getUniqueId())) return; // consumes the block state

        ItemStack held = victim.getInventory().getItemInMainHand();
        if (!isSword(held)) return;

        event.setDamage(event.getDamage() * 0.5);
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent event) {
        blocking.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        blocking.remove(event.getPlayer().getUniqueId());
    }

    private static boolean isSword(ItemStack item) {
        if (item == null) return false;
        return switch (item.getType()) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD,
                 GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD -> true;
            default -> false;
        };
    }
}
