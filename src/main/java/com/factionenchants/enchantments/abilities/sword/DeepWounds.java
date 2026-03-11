package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import local.simplepvp.SimplePvPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Deep Wounds III — Sword enchantment, UNIQUE tier.
 * Increases the chance of inflicting a bleed effect (damage over time) on the target.
 * Each bleed tick combat tags the victim.
 */
public class DeepWounds extends CustomEnchantment {

    private static final Random random = new Random();
    private static final Map<UUID, Integer> taskMap = new HashMap<>();

    public DeepWounds() {
        super("deepwounds", "Deep Wounds", 3, EnchantTier.UNIQUE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "25% chance per level to inflict bleed effect (damage over time with slowness).";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 25% chance per level (25%, 50%, 75%)
        if (random.nextInt(100) >= level * 25) return;

        Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;

        UUID tid = target.getUniqueId();

        // Cancel any existing bleed task on this target
        Integer oldTask = taskMap.get(tid);
        if (oldTask != null) Bukkit.getScheduler().cancelTask(oldTask);

        int maxTicks = 2 + level; // level 1 → 3 ticks, level 3 → 5 ticks (3-5 seconds)
        int[] ticks = {0};

        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!target.isValid() || target.isDead() || ticks[0] >= maxTicks) {
                Bukkit.getScheduler().cancelTask(taskMap.getOrDefault(tid, -1));
                taskMap.remove(tid);
                return;
            }
            // Deal bleed damage
            target.damage(level * 0.5, attacker);
            // Apply slowness
            int slownessAmplifier = Math.min(level - 1, 2); // Slowness I-III
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 25, slownessAmplifier, false, false), true);
            
            // Combat tag the victim if they're a player
            if (target instanceof Player playerVictim) {
                Plugin pvpPlugin = Bukkit.getPluginManager().getPlugin("SimplePvP");
                if (pvpPlugin instanceof SimplePvPPlugin simplePvP) {
                    simplePvP.getCombatTagManager().tag(playerVictim);
                }
            }
            
            ticks[0]++;
        }, 20L, 20L); // Every 1 second

        taskMap.put(tid, task);
    }
}
