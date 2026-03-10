package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Angelic — Armor enchantment, ULTIMATE tier, max level V.
 * Heals health over time whenever you are damaged.
 * The enchantment IS stackable across armor pieces in terms of activation
 * chance, but only 1 active healing task can exist per player at a time.
 *
 * Heal amount per tick: 0.5 hearts.
 * Heal ticks:          level + 1 (ticks at 20-tick / 1-second intervals).
 * Activation chance:   level × 15% per armor piece.
 */
public class Angelic extends CustomEnchantment {

    /** Maps player UUID → currently running heal task ID (-1 = none). */
    private static final Map<UUID, Integer> activeTasks = new ConcurrentHashMap<>();

    private static final double HEAL_PER_TICK = 0.5;
    private static final Random random = new Random();

    public Angelic() {
        super("angelic", "Angelic", 5, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Heals health over time whenever damaged; this enchantment IS stackable in terms of " +
               "activation chance; however you can only have 1 active healing task from Angelic at any given time.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Activation chance: level × 15%
        if (random.nextInt(100) >= level * 15) return;

        // If a heal task is already running for this player, don't stack
        UUID id = defender.getUniqueId();
        if (activeTasks.containsKey(id)) return;

        Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;

        int totalTicks = level + 1; // level 1 → 2 ticks, level 5 → 6 ticks
        int[] ticks = {0};

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!defender.isOnline() || ticks[0] >= totalTicks) {
                int tid = activeTasks.getOrDefault(id, -1);
                if (tid != -1) Bukkit.getScheduler().cancelTask(tid);
                activeTasks.remove(id);
                return;
            }
            double maxHp = defender.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double newHp = Math.min(maxHp, defender.getHealth() + HEAL_PER_TICK);
            defender.setHealth(newHp);
            ticks[0]++;
        }, 20L, 20L);

        activeTasks.put(id, taskId);
    }
}
