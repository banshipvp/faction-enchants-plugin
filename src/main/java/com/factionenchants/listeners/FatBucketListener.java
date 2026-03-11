package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Enforces liquid restrictions on Lava/Water Fat Buckets purchased from the XP Shop.
 * - Lava Fat Bucket can only be refilled with lava.
 * - Water Fat Bucket can only be refilled with water.
 * PDC keys are preserved through empty/fill cycles.
 */
public class FatBucketListener implements Listener {

    private final NamespacedKey lavaKey;
    private final NamespacedKey waterKey;

    public FatBucketListener(FactionEnchantsPlugin plugin) {
        this.lavaKey  = new NamespacedKey(plugin, "lava_fat_bucket");
        this.waterKey = new NamespacedKey(plugin, "water_fat_bucket");
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = event.getHand() == EquipmentSlot.HAND
                ? player.getInventory().getItemInMainHand()
                : player.getInventory().getItemInOffHand();

        boolean isLava  = has(hand, lavaKey);
        boolean isWater = has(hand, waterKey);
        if (!isLava && !isWater) return;

        // event.getItemStack() is the resulting filled bucket
        Material filled = event.getItemStack().getType();

        if (isLava && filled == Material.WATER_BUCKET) {
            event.setCancelled(true);
            player.sendMessage("§cLava Fat Bucket can only be refilled with §4lava§c!");
            return;
        }
        if (isWater && filled == Material.LAVA_BUCKET) {
            event.setCancelled(true);
            player.sendMessage("§cWater Fat Bucket can only be refilled with §bwater§c!");
            return;
        }

        // Carry PDC key onto the resulting filled bucket
        ItemStack result = event.getItemStack().clone();
        ItemMeta meta = result.getItemMeta();
        if (meta == null) return;
        meta.getPersistentDataContainer().set(isLava ? lavaKey : waterKey, PersistentDataType.BYTE, (byte) 1);
        result.setItemMeta(meta);
        event.setItemStack(result);
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = event.getHand() == EquipmentSlot.HAND
                ? player.getInventory().getItemInMainHand()
                : player.getInventory().getItemInOffHand();

        boolean isLava  = has(hand, lavaKey);
        boolean isWater = has(hand, waterKey);
        if (!isLava && !isWater) return;

        // event.getItemStack() is the empty BUCKET that results
        ItemStack result = event.getItemStack().clone();
        ItemMeta meta = result.getItemMeta();
        if (meta == null) return;
        meta.getPersistentDataContainer().set(isLava ? lavaKey : waterKey, PersistentDataType.BYTE, (byte) 1);
        result.setItemMeta(meta);
        event.setItemStack(result);
    }

    private boolean has(ItemStack item, NamespacedKey key) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}
