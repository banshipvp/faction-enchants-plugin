package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.EnchantmentManager;
import com.factionenchants.items.TransmogScrollItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Handles Transmog Scroll application.
 *
 * Right-click with scroll in main hand → sorts enchants on off-hand item.
 * Right-click with scroll in off hand  → sorts enchants on main-hand item.
 * Enchants are sorted highest rarity first (MASTERY → SIMPLE).
 */
public class TransmogScrollListener implements Listener {

    /** Tier sort order — index 0 = first shown (highest rarity). */
    private static final List<CustomEnchantment.EnchantTier> TIER_ORDER = List.of(
            CustomEnchantment.EnchantTier.MASTERY,
            CustomEnchantment.EnchantTier.HEROIC,
            CustomEnchantment.EnchantTier.SOUL,
            CustomEnchantment.EnchantTier.LEGENDARY,
            CustomEnchantment.EnchantTier.ULTIMATE,
            CustomEnchantment.EnchantTier.ELITE,
            CustomEnchantment.EnchantTier.UNIQUE,
            CustomEnchantment.EnchantTier.SIMPLE
    );

    private final FactionEnchantsPlugin plugin;

    public TransmogScrollListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack hand = event.getItem();
        if (hand == null || !TransmogScrollItem.isTransmogScroll(plugin, hand)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        EquipmentSlot scrollSlot = event.getHand();

        ItemStack target = (scrollSlot == EquipmentSlot.HAND)
                ? player.getInventory().getItemInOffHand()
                : player.getInventory().getItemInMainHand();

        String otherHand = (scrollSlot == EquipmentSlot.HAND) ? "off-hand" : "main hand";

        if (!isApplicableGear(target)) {
            player.sendMessage("§cHold applicable gear in your " + otherHand + " to apply the Transmog Scroll!");
            return;
        }

        Map<CustomEnchantment, Integer> enchants =
                plugin.getEnchantmentManager().getEnchantmentsOnItem(target);
        if (enchants.isEmpty()) {
            player.sendMessage("§cThat item has no custom enchants to sort!");
            return;
        }

        ItemStack updated = target.clone();
        ItemMeta meta = updated.getItemMeta();
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

        // Remove existing enchant lore lines
        for (CustomEnchantment enchant : enchants.keySet()) {
            String plainName = enchant.getDisplayName().replaceAll("§[0-9a-fk-or]", "").trim();
            lore.removeIf(l -> l.replaceAll("§[0-9a-fk-or]", "").trim().startsWith(plainName + " "));
        }

        // Sort by tier (highest rarity shown first) then append
        List<Map.Entry<CustomEnchantment, Integer>> sorted = new ArrayList<>(enchants.entrySet());
        sorted.sort(Comparator.comparingInt(e -> {
            int idx = TIER_ORDER.indexOf(e.getKey().getTier());
            return idx < 0 ? TIER_ORDER.size() : idx;
        }));

        for (Map.Entry<CustomEnchantment, Integer> entry : sorted) {
            CustomEnchantment enchant = entry.getKey();
            int level = entry.getValue();
            lore.add("§" + enchant.getTier().getColor() + enchant.getDisplayName()
                    + " " + EnchantmentManager.toRoman(level));
        }

        meta.setLore(lore);
        updated.setItemMeta(meta);

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

        player.sendMessage("§e✦ §aEnchants sorted by rarity!");
        player.updateInventory();
    }

    private boolean isApplicableGear(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        String name = item.getType().name();
        if (name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS")) return true;
        if (name.endsWith("_SWORD") || name.endsWith("_AXE")) return true;
        if (name.endsWith("_PICKAXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE")) return true;
        Material m = item.getType();
        return m == Material.BOW || m == Material.CROSSBOW || m == Material.TRIDENT
                || m == Material.FISHING_ROD || m == Material.SHIELD || m == Material.ELYTRA;
    }
}
