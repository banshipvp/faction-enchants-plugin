package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Corrupt extends CustomEnchantment {

    private final Random random = new Random();
    private static final Map<UUID, Integer> taskMap = new HashMap<>();

    public Corrupt() {
        super("corrupt", "Corrupt", 4, EnchantTier.ULTIMATE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Damage with niche — this enchant deals damage over time.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            UUID tid = target.getUniqueId();
            Integer oldTask = taskMap.get(tid);
            if (oldTask != null) Bukkit.getScheduler().cancelTask(oldTask);
            Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchantsPlugin");
            if (plugin == null) return;
            int[] ticks = {0};
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (!target.isValid() || target.isDead() || ticks[0] >= level * 3) {
                    Bukkit.getScheduler().cancelTask(taskMap.getOrDefault(tid, -1));
                    taskMap.remove(tid);
                    return;
                }
                target.damage(0.5 + level * 0.5, attacker);
                ticks[0]++;
            }, 20L, 20L);
            taskMap.put(tid, task);
        }
    }
}
