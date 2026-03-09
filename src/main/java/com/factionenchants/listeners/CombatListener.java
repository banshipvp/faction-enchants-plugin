package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.abilities.armor.BloodLink;
import com.factionenchants.enchantments.abilities.armor.Hex;
import com.factionenchants.enchantments.abilities.armor.Lifebloom;
import com.factionenchants.enchantments.abilities.sword.AntiGank;
import com.factionenchants.enchantments.abilities.sword.Silence;
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
                // Weapon enchants
                for (Map.Entry<CustomEnchantment, Integer> e : weaponEnchants.entrySet()) {
                    int soulCost = e.getKey().getSoulCostPerProc();
                    if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(attacker, soulCost)) continue;
                    e.getKey().onHitEntity(attacker, target, e.getValue(), event);
                }
                // Reflect damage back if the attacker is Hexed
                if (event.getDamager() instanceof Player hexedAttacker
                        && Hex.isHexed(hexedAttacker.getUniqueId())) {
                    // Find the highest Hex level on any axe the hexed player carries — use stored level
                    double reflectRatio = Hex.getReflectRatio(hexedAttacker.getUniqueId(), 1); // default ratio
                    double reflected = event.getDamage() * reflectRatio;
                    if (reflected > 0) hexedAttacker.damage(reflected);
                }
                // Attacker armor enchants (for offensive armor like Shuffle)
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
            ItemStack bow = shooter.getInventory().getItemInMainHand();
            Map<CustomEnchantment, Integer> bowEnchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(bow);
            if (event.getEntity() instanceof LivingEntity target) {
                // Bow enchants
                for (Map.Entry<CustomEnchantment, Integer> e : bowEnchants.entrySet()) {
                    int soulCost = e.getKey().getSoulCostPerProc();
                    if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(shooter, soulCost)) continue;
                    e.getKey().onArrowHit(shooter, target, e.getValue(), event);
                }
                // Shooter armor enchants (for Marksman etc.)
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
        // Trident melee (treat as melee weapon)
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
            boolean silenced = Silence.isSilenced(defender.getUniqueId());
            for (ItemStack armor : defender.getInventory().getArmorContents()) {
                if (armor == null) continue;
                for (Map.Entry<CustomEnchantment, Integer> e : plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                    if (silenced) continue; // Silence suppresses all defensive armour enchants
                    int soulCost = e.getKey().getSoulCostPerProc();
                    if (soulCost > 0 && !plugin.getSoulManager().consumeSouls(defender, soulCost)) continue;
                    e.getKey().onHurtBy(defender, event.getDamager(), e.getValue(), event);
                }
            }
            // Record hit for AntiGank tracking so that bearer knows they're being ganked
            if (event.getDamager() instanceof Player attackerPlayer) {
                AntiGank.recordHit(defender.getUniqueId(), attackerPlayer.getUniqueId());
            }
        }
    }

    /** Trigger BloodLink when a Guardians golem takes damage. */
    @EventHandler
    public void onGolemDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof IronGolem golem)) return;
        BloodLink.golemDamaged(golem.getUniqueId(), plugin);
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
        // Handle special death enchants like Lifebloom
        for (ItemStack armor : event.getEntity().getEquipment().getArmorContents()) {
            if (armor == null) continue;
            for (Map.Entry<CustomEnchantment, Integer> e : plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                if (e.getKey() instanceof Lifebloom lb) {
                    lb.onWearerDeath((Player) event.getEntity(), e.getValue());
                }
            }
        }
    }
}
