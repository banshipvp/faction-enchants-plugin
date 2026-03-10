package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.items.EnchantmentOrbItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Handles Weapon and Armor Enchantment Orb usage.
 *
 * Usage: drag an orb onto a piece of applicable gear in any inventory.
 * - 95% success → the gear's max enchantment slot cap is raised by 1
 *   (if it has not yet reached the orb's cap).
 * - 5% failure  → the orb is consumed with no effect.
 */
public class EnchantmentOrbListener implements Listener {

    private final FactionEnchantsPlugin plugin;
    private final Random random = new Random();

    public EnchantmentOrbListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack cursor  = event.getCursor();
        ItemStack clicked = event.getCurrentItem();

        if (cursor == null || clicked == null) return;

        boolean isWeaponOrb = EnchantmentOrbItem.isWeaponOrb(plugin, cursor);
        boolean isArmorOrb  = EnchantmentOrbItem.isArmorOrb(plugin, cursor);

        if (!isWeaponOrb && !isArmorOrb) return;

        // Validate that the clicked gear matches the orb type
        if (isWeaponOrb && !EnchantmentOrbItem.isWeaponGear(clicked)) {
            player.sendMessage("§cYou can only apply Weapon Enchantment Orbs to weapons!");
            event.setCancelled(true);
            return;
        }
        if (isArmorOrb && !EnchantmentOrbItem.isArmorGear(clicked)) {
            player.sendMessage("§cYou can only apply Armor Enchantment Orbs to armor!");
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        int orbMaxSlots  = EnchantmentOrbItem.getOrbMaxSlots(plugin, cursor);
        int gearCurrent  = EnchantmentOrbItem.getItemMaxSlots(plugin, clicked);
        // If gear has no custom slots yet, treat its current cap as the global default (9)
        if (gearCurrent < 0) gearCurrent = 9;

        if (gearCurrent >= orbMaxSlots) {
            player.sendMessage("§cThis item already has §e" + gearCurrent
                    + "§c max enchantment slots — you need a higher-tier orb to increase it further!");
            return;
        }

        // Consume one orb from cursor
        consumeOne(event, cursor);

        // 95% success chance
        if (random.nextInt(100) >= 95) {
            player.sendMessage("§cThe orb shattered! The enchantment slot application §4failed§c.");
            return;
        }

        // Success — increment gear's max slots
        int newMax = gearCurrent + 1;
        EnchantmentOrbItem.setItemMaxSlots(plugin, clicked, newMax);

        String type = isWeaponOrb ? "weapon" : "armor";
        player.sendMessage("§aSuccessfully expanded your §e" + type
                + "§a's max enchantment slots to §e" + newMax + "§a!");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Decrements the cursor item by 1, removing it entirely if the last one. */
    private static void consumeOne(InventoryClickEvent event, ItemStack cursor) {
        if (cursor.getAmount() > 1) {
            cursor.setAmount(cursor.getAmount() - 1);
            event.setCursor(cursor);
        } else {
            event.setCursor(null);
        }
    }
}
