package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.abilities.sword.ExtendedLooting;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles the extra drops granted by the {@link ExtendedLooting} custom enchant.
 *
 * <p>Runs at HIGHEST priority so vanilla looting (applied by Bukkit at NORMAL
 * priority) has already set the base drop list before we augment it.
 */
public class ExtendedLootingListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public ExtendedLootingListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer == null) return;

        ItemStack weapon = killer.getInventory().getItemInMainHand();
        Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(weapon);

        Integer enchantLevel = null;
        for (Map.Entry<CustomEnchantment, Integer> entry : enchants.entrySet()) {
            if (entry.getKey() instanceof ExtendedLooting) {
                enchantLevel = entry.getValue();
                break;
            }
        }

        if (enchantLevel == null) return;

        int extraPerItem = ExtendedLooting.extraDropsPerItem(enchantLevel);
        if (extraPerItem <= 0) return;

        // Make a snapshot first so we don't iterate a list we're modifying
        List<ItemStack> currentDrops = new ArrayList<>(event.getDrops());
        List<ItemStack> extras = new ArrayList<>();

        for (ItemStack drop : currentDrops) {
            if (drop == null) continue;
            // Add `extraPerItem` copies of each drop item (1 at a time to respect stack-size limits)
            for (int i = 0; i < extraPerItem; i++) {
                ItemStack extra = drop.clone();
                // Ensure we don't exceed the material's max stack size
                int clampedAmount = Math.min(drop.getAmount(), drop.getMaxStackSize() - drop.getAmount());
                if (clampedAmount <= 0) clampedAmount = 1;
                extra.setAmount(clampedAmount);
                extras.add(extra);
            }
        }

        event.getDrops().addAll(extras);
    }
}
