package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.abilities.armor.AvengingAngel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Listens for player deaths and triggers {@link AvengingAngel} for nearby
 * players wearing the enchantment within {@link AvengingAngel#TRIGGER_RADIUS} blocks.
 * The dead player themselves does not receive the effect.
 */
public class AvengingAngelListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public AvengingAngelListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deceased = event.getEntity();

        // Check all online players near the deceased
        for (Player nearby : deceased.getWorld().getPlayers()) {
            if (nearby.equals(deceased)) continue;
            if (nearby.getLocation().distance(deceased.getLocation()) > AvengingAngel.TRIGGER_RADIUS) continue;

            // Check if this nearby player has Avenging Angel on any armor piece
            for (ItemStack armor : nearby.getInventory().getArmorContents()) {
                if (armor == null) continue;
                Map<CustomEnchantment, Integer> enchants =
                        plugin.getEnchantmentManager().getEnchantmentsOnItem(armor);
                for (Map.Entry<CustomEnchantment, Integer> entry : enchants.entrySet()) {
                    if (entry.getKey() instanceof AvengingAngel aa) {
                        aa.triggerEffect(nearby, entry.getValue(), plugin);
                        break; // only trigger once per player (highest-level piece wins first match)
                    }
                }
            }
        }
    }
}
