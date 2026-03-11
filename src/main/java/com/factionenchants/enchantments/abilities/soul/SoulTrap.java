package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SoulTrap extends CustomEnchantment {

    private final Random random = new Random();
    private static final Map<UUID, Integer> activeTaskMap = new HashMap<>();

    public SoulTrap() {
        super("soul_trap", "Soul Trap", 3, EnchantTier.SOUL, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Active Soul Enchantment. Your axe is imbued with sealing magic, and has a chance to disable/negate all soul enchantments of your enemies and drain their souls on hit for (level x 4) seconds. Costs 5 souls per second.";
    }

    @Override
    public int getSoulCostPerProc() {
        return 5;
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;

        // Chance: 20% per level
        int chance = level * 20;
        if (random.nextInt(100) >= chance) return;

        UUID victimId = victim.getUniqueId();

        // Cancel any existing soul-trap task on this victim
        Integer existing = activeTaskMap.get(victimId);
        if (existing != null) Bukkit.getScheduler().cancelTask(existing);

        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchantsPlugin");
        if (plugin == null) return;

        // Force-deactivate victim's soul gem
        plugin.getSoulManager().forceDeactivate(victim);
        victim.sendMessage("\u00a74\u2736 Your Soul Gem has been disabled by Soul Trap!");
        victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_SOUL_SAND_PLACE, 1.0f, 0.5f);
        attacker.sendMessage("\u00a7c\u2736 Soul Trap activated!");

        int durationTicks = level * 4 * 20; // level*4 seconds in ticks
        int[] elapsed = {0};
        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            elapsed[0] += 20;
            // Keep the soul gem deactivated while trap is active
            plugin.getSoulManager().forceDeactivate(victim);

            if (elapsed[0] >= durationTicks) {
                Bukkit.getScheduler().cancelTask(activeTaskMap.getOrDefault(victimId, -1));
                activeTaskMap.remove(victimId);
                victim.sendMessage("\u00a7a\u2736 Soul Trap has worn off.");
            }
        }, 20L, 20L);
        activeTaskMap.put(victimId, task);
    }
}
