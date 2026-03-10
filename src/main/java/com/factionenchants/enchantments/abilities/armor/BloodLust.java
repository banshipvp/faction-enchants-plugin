package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.abilities.sword.Bleed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

/**
 * BloodLust — Chestplate enchantment.
 * When an enemy within a 7-block radius is suffering from a Bleed DoT, the wearer
 * has a level-scaled chance to heal 1HP each Bleed tick.
 */
public class BloodLust extends CustomEnchantment {

    private static final Random random = new Random();

    public BloodLust() {
        super("bloodlust", "Blood Lust", 6, EnchantTier.LEGENDARY, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "A chance to heal you whenever an enemy player within 7x7 blocks is damaged by the Bleed enchantment.";
    }

    /**
     * Called by Bleed.java on each damage tick.
     * Scans nearby online players who have BloodLust on their chestplate.
     */
    public static void triggerForNearby(LivingEntity bleedTarget, org.bukkit.plugin.Plugin pluginInstance) {
        if (!(pluginInstance instanceof FactionEnchantsPlugin plugin)) return;

        for (Entity nearby : bleedTarget.getNearbyEntities(7, 7, 7)) {
            if (!(nearby instanceof Player player)) continue;
            if (!Bleed.BLEED_VICTIMS.contains(bleedTarget.getUniqueId())) continue;

            ItemStack chest = player.getInventory().getChestplate();
            if (chest == null) continue;

            for (Map.Entry<CustomEnchantment, Integer> e
                    : plugin.getEnchantmentManager().getEnchantmentsOnItem(chest).entrySet()) {
                if (!(e.getKey() instanceof BloodLust)) continue;
                int level = e.getValue();
                // Chance: 10% per level (10–60% at level 6)
                if (random.nextInt(100) < level * 10) {
                    double heal = 0.5 + level * 0.5;
                    player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + heal));
                }
            }
        }
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Passive — triggered via Bleed DoT, not directly from damage events.
    }
}
