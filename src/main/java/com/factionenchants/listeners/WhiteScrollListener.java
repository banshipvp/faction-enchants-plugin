package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.items.WhiteScrollItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Handles White Scroll application.
 *
 * Right-click while holding the scroll:
 *  - If in main hand → applies to off-hand item (if valid gear)
 *  - If in off hand  → applies to main-hand item (if valid gear)
 * Also prevents the MAP item from opening as a regular map.
 */
public class WhiteScrollListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public WhiteScrollListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        // Only process the hand that currently holds the scroll
        ItemStack hand = event.getItem();
        if (hand == null || !WhiteScrollItem.isWhiteScroll(plugin, hand)) return;

        // Prevent double-firing: only handle once per click (the hand holding the scroll)
        event.setCancelled(true);

        Player player = event.getPlayer();
        EquipmentSlot scrollSlot = event.getHand();

        // Target = the other hand's item
        ItemStack target = (scrollSlot == EquipmentSlot.HAND)
                ? player.getInventory().getItemInOffHand()
                : player.getInventory().getItemInMainHand();

        String otherHandName = (scrollSlot == EquipmentSlot.HAND) ? "off-hand" : "main hand";

        if (!isApplicableGear(target)) {
            player.sendMessage("§cHold applicable gear in your " + otherHandName
                    + " to apply the White Scroll!");
            return;
        }

        if (WhiteScrollItem.isProtected(plugin, target)) {
            player.sendMessage("§cThat item is already §fPROTECTED§c!");
            return;
        }

        // Apply protection to the target item
        ItemStack updated = target.clone();
        WhiteScrollItem.applyProtection(plugin, updated);

        if (scrollSlot == EquipmentSlot.HAND) {
            player.getInventory().setItemInOffHand(updated);
        } else {
            player.getInventory().setItemInMainHand(updated);
        }

        // Consume one scroll
        if (hand.getAmount() > 1) {
            hand.setAmount(hand.getAmount() - 1);
        } else {
            if (scrollSlot == EquipmentSlot.HAND) {
                player.getInventory().setItemInMainHand(null);
            } else {
                player.getInventory().setItemInOffHand(null);
            }
        }

        player.sendMessage("§f✦ §aYour item is now §fPROTECTED §aby a White Scroll!");
        player.sendMessage("§7If an enchant book destroys this item, the White Scroll will absorb the hit.");
        player.updateInventory();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean isApplicableGear(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        String name = item.getType().name();
        // Armor
        if (name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS")) return true;
        // Weapons
        if (name.endsWith("_SWORD") || name.endsWith("_AXE")) return true;
        // Tools
        if (name.endsWith("_PICKAXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE")) return true;
        // Misc gear
        Material m = item.getType();
        return m == Material.BOW
                || m == Material.CROSSBOW
                || m == Material.TRIDENT
                || m == Material.FISHING_ROD
                || m == Material.SHIELD
                || m == Material.ELYTRA;
    }
}
