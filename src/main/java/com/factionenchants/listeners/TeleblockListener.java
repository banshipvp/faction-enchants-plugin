package com.factionenchants.listeners;

import com.factionenchants.enchantments.abilities.soul.Teleblock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Cancels enderpearl throws for players currently under Teleblock.
 */
public class TeleblockListener implements Listener {

    @EventHandler
    public void onPearlUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Teleblock.isTeleblocked(player.getUniqueId())) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.ENDER_PEARL) return;

        event.setCancelled(true);
        player.sendMessage("\u00a7c\u2736 You cannot use Enderpearls while Teleblocked!");
    }
}
