package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;

/**
 * Double Strike III — Sword enchantment (LEGENDARY).
 * Gives a (level * 15)% chance to attack twice in one swing.
 * All weapon enchantments (except Double Strike itself) re-proc on the second hit instantly.
 *
 * A recursion guard via {@link #STRIKING} prevents infinite loops.
 */
public class DoubleStrike extends CustomEnchantment {

    /** Guards against recursive re-procs. */
    public static final Set<UUID> STRIKING = Collections.synchronizedSet(new HashSet<>());

    public DoubleStrike() {
        super("double_strike", "Double Strike", 3, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to attack twice in one swing. All your enchantments can re-proc on this second attack, and it occurs instantly.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Prevent recursive triggering
        if (STRIKING.contains(attacker.getUniqueId())) return;

        double procChance = level * 0.15;
        if (Math.random() >= procChance) return;

        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;

        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(weapon);

        // Also include armor enchants for the second strike
        ItemStack[] armorContents = attacker.getInventory().getArmorContents();

        STRIKING.add(attacker.getUniqueId());
        try {
            // Re-fire weapon enchants (skip self)
            for (Map.Entry<CustomEnchantment, Integer> e : enchants.entrySet()) {
                if (e.getKey() instanceof DoubleStrike) continue;
                e.getKey().onHitEntity(attacker, target, e.getValue(), event);
            }
            // Re-fire armor enchants
            for (ItemStack armor : armorContents) {
                if (armor == null) continue;
                for (Map.Entry<CustomEnchantment, Integer> e : plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                    e.getKey().onHitEntity(attacker, target, e.getValue(), event);
                }
            }
        } finally {
            STRIKING.remove(attacker.getUniqueId());
        }
    }
}
