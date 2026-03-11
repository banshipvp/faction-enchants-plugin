package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Dispatches player-death events to armor enchantments.
 */
public class DeathListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public DeathListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType().isAir()) continue;
            for (Map.Entry<CustomEnchantment, Integer> e :
                    plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                e.getKey().onPlayerDeath(player, e.getValue(), armor, event);
            }
        }
    }
}
