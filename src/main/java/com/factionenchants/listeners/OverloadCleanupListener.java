package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

/**
 * Cleans up Overload potion effects when players join the server or periodically if they don't have Overload armor.
 * This handles cases where players logged out or the server restarted while they had Overload effects.
 */
public class OverloadCleanupListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public OverloadCleanupListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        // Run cleanup task every 5 seconds to catch any stuck effects
        Bukkit.getScheduler().runTaskTimer(plugin, this::cleanupOverloadEffects, 100L, 100L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Clean up on join
        Bukkit.getScheduler().runTaskLater(plugin, () -> cleanupOverloadEffect(event.getPlayer()), 20L);
    }

    /**
     * Periodically clean up Overload effects for all online players who don't have Overload armor.
     */
    private void cleanupOverloadEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            cleanupOverloadEffect(player);
        }
    }

    /**
     * Remove health boost and absorption effects if the player doesn't have Overload armor equipped.
     */
    private void cleanupOverloadEffect(Player player) {
        // Check if player has Overload on any armor piece
        boolean hasOverload = false;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType().isAir()) continue;
            Map<CustomEnchantment, Integer> enchants = 
                    plugin.getEnchantmentManager().getEnchantmentsOnItem(armor);
            for (CustomEnchantment ench : enchants.keySet()) {
                if (ench.getId().equalsIgnoreCase("overload")) {
                    hasOverload = true;
                    break;
                }
            }
            if (hasOverload) break;
        }

        // If no Overload armor, clear health boost and absorption effects
        if (!hasOverload) {
            player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
            player.removePotionEffect(PotionEffectType.ABSORPTION);
        }
    }
}
