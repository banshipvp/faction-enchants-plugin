package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

/**
 * Cleans up Overload potion effects when players join the server.
 * This handles cases where players logged out or the server restarted while they had Overload effects.
 */
public class OverloadCleanupListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public OverloadCleanupListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
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
