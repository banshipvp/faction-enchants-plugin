package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Dispatches block-break and block-damage events to tool enchantments.
 */
public class ToolListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public ToolListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack held = player.getInventory().getItemInMainHand();
        for (Map.Entry<CustomEnchantment, Integer> e :
                plugin.getEnchantmentManager().getEnchantmentsOnItem(held).entrySet()) {
            e.getKey().onBlockBreak(player, event.getBlock(), e.getValue(), event);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack held = player.getInventory().getItemInMainHand();
        for (Map.Entry<CustomEnchantment, Integer> e :
                plugin.getEnchantmentManager().getEnchantmentsOnItem(held).entrySet()) {
            e.getKey().onBlockDamage(player, event.getBlock(), e.getValue(), event);
        }
    }
}
