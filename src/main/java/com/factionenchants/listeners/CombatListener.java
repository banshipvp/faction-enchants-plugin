package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.abilities.bow.Unfocus;
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
            Map<CustomEnchantment, Integer> weaponEnchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(weapon);
            if (event.getEntity() instanceof LivingEntity target) {
                for (Map.Entry<CustomEnchantment, Integer> e : weaponEnchants.entrySet()) {
                    int soulCost = e.getKey().getSoulCostPerProc();
                    if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(attacker, soulCost)) continue;
                    e.getKey().onHitEntity(attacker, target, e.getValue(), event);
                }
                for (ItemStack armor : attacker.getInventory().getArmorContents()) {
                    if (armor == null) continue;
                    for (Map.Entry<CustomEnchantment, Integer> e : plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                        int soulCost = e.getKey().getSoulCostPerProc();
                        if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(attacker, soulCost)) continue;
                        e.getKey().onHitEntity(attacker, target, e.getValue(), event);
                    }
                }
            }
        }
        // Arrow hit
        else if (event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player shooter) {
            // Unfocus debuff — unfocused shooter deals 50% less bow damage
            if (Unfocus.isUnfocused(shooter.getUniqueId())) {
                event.setDamage(event.getDamage() * (1.0 - Unfocus.REDUCTION));
            }
            ItemStack bow = shooter.getInventory().getItemInMainHand();
            Map<CustomEnchantment, Integer> bowEnchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(bow);
            if (event.getEntity() instanceof LivingEntity target) {
                for (Map.Entry<CustomEnchantment, Integer> e : bowEnchants.entrySet()) {
                    int soulCost = e.getKey().getSoulCostPerProc();
                    if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(shooter, soulCost)) continue;
                    e.getKey().onArrowHit(shooter, target, e.getValue(), event);
                }
                for (ItemStack armor : shooter.getInventory().getArmorContents()) {
                    if (armor == null) continue;
                    for (Map.Entry<CustomEnchantment, Integer> e : plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                        int soulCost = e.getKey().getSoulCostPerProc();
                        if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(shooter, soulCost)) continue;
                        e.getKey().onArrowHit(shooter, target, e.getValue(), event);
                    }
                }
            }
        }
        // Trident melee
        else if (event.getDamager() instanceof Trident trident && trident.getShooter() instanceof Player attacker) {
            ItemStack tridentItem = trident.getItem();
            Map<CustomEnchantment, Integer> tridentEnchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(tridentItem);
            if (event.getEntity() instanceof LivingEntity target) {
                for (Map.Entry<CustomEnchantment, Integer> e : tridentEnchants.entrySet()) {
                    int soulCost = e.getKey().getSoulCostPerProc();
                    if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(attacker, soulCost)) continue;
                    e.getKey().onHitEntity(attacker, target, e.getValue(), event);
                }
            }
        }

        // Defender armor
        if (event.getEntity() instanceof Player defender) {
            for (ItemStack armor : defender.getInventory().getArmorContents()) {
                if (armor == null) continue;
                for (Map.Entry<CustomEnchantment, Integer> e : plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                    int soulCost = e.getKey().getSoulCostPerProc();
                    if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(defender, soulCost)) continue;
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
        Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(weapon);
        for (Map.Entry<CustomEnchantment, Integer> e : enchants.entrySet()) {
            int soulCost = e.getKey().getSoulCostPerProc();
            if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(killer, soulCost)) continue;
            e.getKey().onKillEntity(killer, event.getEntity(), e.getValue(), weapon);
        }
    }
}