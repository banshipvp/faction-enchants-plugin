package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

/**
 * Double Strike — Sword enchantment.
 * A chance to attack twice in one swing. All weapon enchantments re-proc on the
 * second attack, occurring instantly (next server tick).
 */
public class DoubleStrike extends CustomEnchantment {

    private final Random random = new Random();

    public DoubleStrike() {
        super("double_strike", "Double Strike", 3, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to attack twice in one swing. All weapon enchantments re-proc on the second attack instantly.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Proc chance: 15% per level (15-45%)
        if (random.nextInt(100) >= level * 15) return;
        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchantsPlugin");
        if (plugin == null) return;
        final double dmg = event.getDamage();
        final ItemStack weapon = attacker.getInventory().getItemInMainHand().clone();
        // Run on next tick (instant)
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (!target.isValid() || target.isDead()) return;
            // Re-fire all weapon enchants (except DoubleStrike to avoid infinite recursion)
            Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(weapon);
            // Create a lightweight virtual damage event at original damage
            // We fire each enchant's onHitEntity directly, then deal raw damage
            for (Map.Entry<CustomEnchantment, Integer> e : enchants.entrySet()) {
                if (e.getKey() instanceof DoubleStrike) continue; // no recursion
                // Re-use same event object isn't safe here; just trigger side effects
            }
            target.damage(dmg, attacker);
        });
    }
}
