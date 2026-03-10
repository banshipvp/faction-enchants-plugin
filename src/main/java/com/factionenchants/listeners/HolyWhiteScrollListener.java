package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.items.HolyWhiteScrollItem;
import com.factionenchants.items.WhiteScrollItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.ArrayList;

/**
 * Handles Holy White Scroll mechanics:
 *
 * 1. Right-click application:
 *    Player holds the Holy White Scroll + holds gear in the other hand.
 *    The gear must already have White Scroll protection.
 *    On apply: White Scroll protection is consumed, Holy blessing is added.
 *    The Holy White Scroll is consumed.
 *
 * 2. Keep-on-death:
 *    When a player dies, any gear item marked with "has_holy_whitescroll" is
 *    removed from the drop list and re-added to the player's inventory on respawn
 *    (via the keep-inventory mechanism in PlayerDeathEvent).
 */
public class HolyWhiteScrollListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public HolyWhiteScrollListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    // ── Right-click application ───────────────────────────────────────────────

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack hand = event.getItem();
        if (hand == null || !HolyWhiteScrollItem.isHolyWhiteScroll(plugin, hand)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        EquipmentSlot scrollSlot = event.getHand();

        // Target = the other hand
        ItemStack target = (scrollSlot == EquipmentSlot.HAND)
                ? player.getInventory().getItemInOffHand()
                : player.getInventory().getItemInMainHand();
        String otherName = (scrollSlot == EquipmentSlot.HAND) ? "off-hand" : "main hand";

        if (!isApplicableGear(target)) {
            player.sendMessage("§cHold applicable gear in your " + otherName + " to apply the Holy White Scroll!");
            return;
        }
        if (!WhiteScrollItem.isProtected(plugin, target)) {
            player.sendMessage("§cThe gear in your " + otherName + " must first have a §fWhite Scroll §capplied!");
            return;
        }
        if (HolyWhiteScrollItem.isBlessed(plugin, target)) {
            player.sendMessage("§cThat item is already §dBLESSED§c!");
            return;
        }

        ItemStack blessed = target.clone();
        WhiteScrollItem.removeProtection(plugin, blessed);
        HolyWhiteScrollItem.applyBlessing(plugin, blessed);

        if (scrollSlot == EquipmentSlot.HAND) {
            player.getInventory().setItemInOffHand(blessed);
        } else {
            player.getInventory().setItemInMainHand(blessed);
        }

        // Consume one scroll
        if (hand.getAmount() > 1) {
            hand.setAmount(hand.getAmount() - 1);
        } else {
            if (scrollSlot == EquipmentSlot.HAND) player.getInventory().setItemInMainHand(null);
            else player.getInventory().setItemInOffHand(null);
        }

        player.sendMessage("§d✦ §aYour item is now §dBLESSED§a! It will be kept on death.");
        player.updateInventory();
    }

    // ── Keep-on-death ─────────────────────────────────────────────────────────

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<ItemStack> drops = event.getDrops();
        List<ItemStack> kept  = new ArrayList<>();

        drops.removeIf(item -> {
            if (HolyWhiteScrollItem.isBlessed(plugin, item)) {
                kept.add(item);
                return true;
            }
            return false;
        });

        if (kept.isEmpty()) return;

        // Return blessed items to the player's inventory on the next tick
        // (after spawn location is set). We use keepInventory-style logic:
        // store in the kept list and add back after respawn via scheduler.
        final List<ItemStack> toReturn = List.copyOf(kept);
        org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player target = player;
            if (!target.isOnline()) {
                // Drop at death location if player somehow disconnected
                for (ItemStack item : toReturn) {
                    player.getWorld().dropItemNaturally(player.getLastDeathLocation() != null
                            ? player.getLastDeathLocation() : player.getLocation(), item);
                }
                return;
            }
            for (ItemStack item : toReturn) {
                var leftovers = target.getInventory().addItem(item);
                leftovers.values().forEach(leftover ->
                        target.getWorld().dropItemNaturally(target.getLocation(), leftover));
            }
            target.sendMessage("§d✦ §aYour §dBLESSED §aitem(s) were kept on death!");
        }, 1L);
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private boolean isApplicableGear(ItemStack item) {
        if (item == null || item.getType() == org.bukkit.Material.AIR) return false;
        String name = item.getType().name();
        if (name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS")) return true;
        if (name.endsWith("_SWORD") || name.endsWith("_AXE")) return true;
        if (name.endsWith("_PICKAXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE")) return true;
        org.bukkit.Material m = item.getType();
        return m == org.bukkit.Material.BOW || m == org.bukkit.Material.CROSSBOW
                || m == org.bukkit.Material.TRIDENT || m == org.bukkit.Material.FISHING_ROD
                || m == org.bukkit.Material.SHIELD || m == org.bukkit.Material.ELYTRA;
    }
}
