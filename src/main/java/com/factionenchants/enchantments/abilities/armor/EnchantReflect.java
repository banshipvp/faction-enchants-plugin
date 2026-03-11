package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Enchant Reflect X — Armor enchantment (LEGENDARY).
 * Each incoming attack has a (level * 8)% chance to reflect one of the attacker's
 * weapon enchants back at them (calling its onHitEntity with the attacker as the
 * target). Acts as a recursion guard to prevent cascading reflections.
 */
public class EnchantReflect extends CustomEnchantment {

    /** Prevents infinite reflect ↔ reflect loops. */
    public static final Set<UUID> REFLECTING = Collections.synchronizedSet(new HashSet<>());

    public EnchantReflect() {
        super("enchant_reflect", "Enchant Reflect", 10, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "8% chance per level to reflect one enemy weapon enchant back at them (reflects enchants ≤ your level).";
    }

    @Override
    public void onHurtBy(Player defender, Entity damager, int level, EntityDamageByEntityEvent event) {
        if (!(damager instanceof Player attacker)) return;
        if (REFLECTING.contains(defender.getUniqueId())) return;

        double procChance = level * 0.08; // 8% per level, up to 80% at X
        if (Math.random() >= procChance) return;

        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;

        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        Map<CustomEnchantment, Integer> weaponEnchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(weapon);
        if (weaponEnchants.isEmpty()) return;

        // Pick a random enchant to reflect
        CustomEnchantment[] keys = weaponEnchants.keySet().toArray(new CustomEnchantment[0]);
        CustomEnchantment toReflect = keys[(int) (Math.random() * keys.length)];
        int reflectLevel = weaponEnchants.get(toReflect);

        // Only reflect up to enchants whose level <= THIS enchant's level
        if (reflectLevel > level) return;
        // Don't reflect Enchant Reflect itself
        if (toReflect instanceof EnchantReflect) return;

        // Mark as reflecting to prevent cascades
        REFLECTING.add(defender.getUniqueId());
        try {
            // Fire the attacker's enchant — targeting the attacker themselves
            toReflect.onHitEntity(attacker, attacker, reflectLevel, event);
        } finally {
            REFLECTING.remove(defender.getUniqueId());
        }
    }
}
