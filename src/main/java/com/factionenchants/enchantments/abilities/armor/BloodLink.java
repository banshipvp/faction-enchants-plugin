package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * BloodLink — Armor enchantment.
 * Heals the wearer when one of their Guardians iron golems takes damage.
 * Guardians.GOLEM_OWNERS tracks which golem belongs to which player.
 */
public class BloodLink extends CustomEnchantment {

    private static final Random random = new Random();

    public BloodLink() {
        super("bloodlink", "Blood Link", 5, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "20% chance per level to heal 0.5-3.0 HP (0.5+level×0.5) when your Guardians golems take damage.";
    }

    /**
     * Called by CombatListener when an IronGolem takes damage.
     * Finds the golem's owner, checks for BloodLink on their armour, and triggers heals.
     */
    public static void golemDamaged(UUID golemId, FactionEnchantsPlugin plugin) {
        UUID ownerUUID = Guardians.GOLEM_OWNERS.get(golemId);
        if (ownerUUID == null) return;
        Player owner = Bukkit.getPlayer(ownerUUID);
        if (owner == null || !owner.isOnline()) return;

        for (ItemStack armor : owner.getInventory().getArmorContents()) {
            if (armor == null) continue;
            for (Map.Entry<CustomEnchantment, Integer> e
                    : plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                if (e.getKey() instanceof BloodLink bl) {
                    bl.tryHeal(owner, e.getValue());
                }
            }
        }
    }

    private void tryHeal(Player player, int level) {
        // Proc chance: 20% per level (20–100%)
        if (random.nextInt(100) >= level * 20) return;
        // Heal 0.5–1.0 HP per level
        double heal = 0.5 + level * 0.5;
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + heal));
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // This enchant reacts to golem damage events, not the wearer's own damage. No-op here.
    }
}
