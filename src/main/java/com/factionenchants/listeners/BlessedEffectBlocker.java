package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.commands.BlessCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Cancels Slowness and Mining Fatigue being applied to blessed players.
 * Players are added to the blessed set by {@link BlessCommand}.
 * Blessed state is cleared when the player removes a Drunk-enchanted helmet or relogs.
 */
public class BlessedEffectBlocker implements Listener {

    private final FactionEnchantsPlugin plugin;

    public BlessedEffectBlocker(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

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

    /**
     * Clears the blessed state when a player removes a Drunk-enchanted helmet
     * from their armor slot. This ensures debuffs resume on re-equip until
     * the player uses /bless or is hit by a Blessed axe again.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArmorSlotClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getSlotType() != InventoryType.SlotType.ARMOR) return;

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getType().isAir()) return;

        // Check if the item being removed from the armor slot has the Drunk enchant
        boolean hasDrunk = plugin.getEnchantmentManager()
                .getEnchantmentsOnItem(currentItem)
                .keySet().stream()
                .anyMatch(e -> "drunk".equals(e.getId()));
        if (!hasDrunk) return;

        // Drunk helmet is being unequipped — clear blessed state so debuffs resume on re-equip
        UUID uid = player.getUniqueId();
        BlessCommand.BLESSED.remove(uid);
        BlessCommand.WAS_BLESSED.remove(uid);
        player.sendMessage("§7[Drunk] §cYour blessing has worn off.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uid = event.getPlayer().getUniqueId();
        BlessCommand.BLESSED.remove(uid);
        BlessCommand.WAS_BLESSED.remove(uid);
    }
}
