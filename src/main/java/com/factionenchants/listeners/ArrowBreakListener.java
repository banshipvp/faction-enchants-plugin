package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.abilities.axe.ArrowBreak;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Cancels arrow damage for players holding an axe with {@link ArrowBreak}
 * when the enchantment procs.
 */
public class ArrowBreakListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public ArrowBreakListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onArrowHit(EntityDamageByEntityEvent event) {
        // Must be an arrow hitting a player
        if (!(event.getDamager() instanceof Arrow)) return;
        if (!(event.getEntity() instanceof Player defender)) return;

        // Check main-hand item for Arrow Break
        ItemStack mainHand = defender.getInventory().getItemInMainHand();
        if (mainHand == null) return;
        if (!mainHand.getType().name().endsWith("_AXE")) return;

        Map<CustomEnchantment, Integer> enchants =
                plugin.getEnchantmentManager().getEnchantmentsOnItem(mainHand);

        for (Map.Entry<CustomEnchantment, Integer> entry : enchants.entrySet()) {
            if (entry.getKey() instanceof ArrowBreak ab) {
                if (ab.shouldDeflect(entry.getValue())) {
                    event.setCancelled(true);
                    defender.sendMessage("§7✦ §fArrow Break deflected an arrow!");
                }
                break;
            }
        }
    }
}
