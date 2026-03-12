package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CombatListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public CombatListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Melee attacker
        if (event.getDamager() instanceof Player attacker) {
            ItemStack weapon = attacker.getInventory().getItemInMainHand();
            Map<CustomEnchantment, Integer> weaponEnchants =
                    plugin.getEnchantmentManager().getEnchantmentsOnItem(weapon);
            if (event.getEntity() instanceof LivingEntity target) {
                for (Map.Entry<CustomEnchantment, Integer> e : weaponEnchants.entrySet()) {
                    e.getKey().onHitEntity(attacker, target, e.getValue(), event);
                }
            }
        }
        // Arrow hit
        else if (event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player shooter) {
            ItemStack bow = shooter.getInventory().getItemInMainHand();
            Map<CustomEnchantment, Integer> bowEnchants =
                    plugin.getEnchantmentManager().getEnchantmentsOnItem(bow);
            if (event.getEntity() instanceof LivingEntity target) {
                for (Map.Entry<CustomEnchantment, Integer> e : bowEnchants.entrySet()) {
                    e.getKey().onArrowHit(shooter, target, e.getValue(), event);
                }
                // Combat-tag both players via SimplePvP
                if (target instanceof Player victim) {
                    org.bukkit.plugin.Plugin pvpPlugin = org.bukkit.Bukkit.getPluginManager().getPlugin("SimplePvP");
                    if (pvpPlugin instanceof local.simplepvp.SimplePvPPlugin simplePvP) {
                        simplePvP.getCombatTagManager().tag(shooter);
                        simplePvP.getCombatTagManager().tag(victim);
                    }
                }
            }
        }

        // Defender armor defensive enchants
        if (event.getEntity() instanceof Player defender) {
            for (ItemStack armor : defender.getInventory().getArmorContents()) {
                if (armor == null) continue;
                for (Map.Entry<CustomEnchantment, Integer> e :
                        plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                    e.getKey().onHurtBy(defender, event.getDamager(), e.getValue(), event);
                }
            }
            // Also check held items for defensive enchants (e.g. Farcast on a bow)
            ItemStack mainHand = defender.getInventory().getItemInMainHand();
            if (!mainHand.getType().isAir()) {
                for (Map.Entry<CustomEnchantment, Integer> e :
                        plugin.getEnchantmentManager().getEnchantmentsOnItem(mainHand).entrySet()) {
                    e.getKey().onHurtBy(defender, event.getDamager(), e.getValue(), event);
                }
            }
            ItemStack offHand = defender.getInventory().getItemInOffHand();
            if (!offHand.getType().isAir()) {
                for (Map.Entry<CustomEnchantment, Integer> e :
                        plugin.getEnchantmentManager().getEnchantmentsOnItem(offHand).entrySet()) {
                    e.getKey().onHurtBy(defender, event.getDamager(), e.getValue(), event);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (!(killer instanceof Player)) return;
        ItemStack weapon = killer.getInventory().getItemInMainHand();
        Map<CustomEnchantment, Integer> enchants =
                plugin.getEnchantmentManager().getEnchantmentsOnItem(weapon);
        for (Map.Entry<CustomEnchantment, Integer> e : enchants.entrySet()) {
            e.getKey().onKillEntity(killer, event.getEntity(), e.getValue(), weapon);
        }
    }
}
