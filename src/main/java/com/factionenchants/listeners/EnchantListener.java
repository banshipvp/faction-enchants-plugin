package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Map;

public class EnchantListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public EnchantListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, this::tickPassiveEffects, 1L, 20L);
    }

    private void tickPassiveEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.CREATIVE
                    || player.getGameMode() == GameMode.SPECTATOR) continue;

            // Tick passive enchants on armor
            for (ItemStack armor : player.getInventory().getArmorContents()) {
                if (armor == null || armor.getType().isAir()) continue;
                for (Map.Entry<CustomEnchantment, Integer> e :
                        plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                    e.getKey().onTickPassive(player, e.getValue(), armor);
                }
            }

            // Tick passive enchants on held tool
            ItemStack held = player.getInventory().getItemInMainHand();
            for (Map.Entry<CustomEnchantment, Integer> e :
                    plugin.getEnchantmentManager().getEnchantmentsOnItem(held).entrySet()) {
                e.getKey().onTickPassive(player, e.getValue(), held);
            }
        }
    }

    /** Handles durability reduction enchants (Hardened, Reforged). */
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        Map<CustomEnchantment, Integer> enchants =
                plugin.getEnchantmentManager().getEnchantmentsOnItem(item);

        int damage = event.getDamage();
        for (Map.Entry<CustomEnchantment, Integer> e : enchants.entrySet()) {
            damage = e.getKey().onItemDamage(player, item, e.getValue(), damage);
        }
        if (damage != event.getDamage()) {
            event.setDamage(damage);
        }
    }

    /** Handles fishing rod enchants that modify hook wait times (Quick Reeler). */
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.FISHING) return;
        Player player = event.getPlayer();
        ItemStack rod = player.getInventory().getItemInMainHand();
        Map<CustomEnchantment, Integer> enchants =
                plugin.getEnchantmentManager().getEnchantmentsOnItem(rod);

        for (Map.Entry<CustomEnchantment, Integer> e : enchants.entrySet()) {
            e.getKey().onFishCast(player, e.getValue(), event);
        }
    }

    /** Handles Repair Guard — gives absorption when low-durability armor is unequipped. */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getSlotType() != InventoryType.SlotType.ARMOR) return;

        InventoryAction action = event.getAction();
        boolean isRemoving = action == InventoryAction.PICKUP_ALL
                || action == InventoryAction.PICKUP_HALF
                || action == InventoryAction.SWAP_WITH_CURSOR
                || action == InventoryAction.HOTBAR_SWAP
                || action == InventoryAction.MOVE_TO_OTHER_INVENTORY;
        if (!isRemoving) return;

        ItemStack armor = event.getCurrentItem();
        if (armor == null || armor.getType().isAir()) return;

        Map<CustomEnchantment, Integer> enchants =
                plugin.getEnchantmentManager().getEnchantmentsOnItem(armor);

        // Handle Overload cleanup (always, regardless of durability)
        for (Map.Entry<CustomEnchantment, Integer> e : enchants.entrySet()) {
            if (e.getKey().getId().equalsIgnoreCase("overload")) {
                e.getKey().onArmorUnequip(player, armor, e.getValue());
            }
        }

        // Handle low-durability armor unequip (Repair Guard, etc.)
        if (armor.getItemMeta() instanceof Damageable damageable) {
            int maxDurability = armor.getType().getMaxDurability();
            if (maxDurability > 0) {
                // Only trigger when armor is 80% or more damaged (20% or less durability remaining)
                if ((double) damageable.getDamage() / maxDurability >= 0.80) {
                    for (Map.Entry<CustomEnchantment, Integer> e : enchants.entrySet()) {
                        e.getKey().onArmorUnequip(player, armor, e.getValue());
                    }
                }
            }
        }
    }
}
