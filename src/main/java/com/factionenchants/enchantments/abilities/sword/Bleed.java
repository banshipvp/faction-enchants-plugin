package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Bleed extends CustomEnchantment {

    private static final Map<UUID, Integer> taskMap = new HashMap<>();
    /** UUIDs of entities currently suffering a Bleed DoT. Used by Devour and BloodLust. */
    public static final Set<UUID> BLEED_VICTIMS = ConcurrentHashMap.newKeySet();

    public Bleed() {
        super("bleed", "Bleed", 6, EnchantTier.ULTIMATE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Makes enemies bleed, dealing damage over time with stacking effects.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchantsPlugin");
        if (plugin == null) return;
        UUID tid = target.getUniqueId();
        Integer oldTask = taskMap.get(tid);
        if (oldTask != null) Bukkit.getScheduler().cancelTask(oldTask);
        final PotionEffectType slow = PotionEffectType.SLOW;
        BLEED_VICTIMS.add(tid);
        int[] ticks = {0};
        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!target.isValid() || target.isDead() || ticks[0] >= level * 4) {
                Bukkit.getScheduler().cancelTask(taskMap.getOrDefault(tid, -1));
                taskMap.remove(tid);
                BLEED_VICTIMS.remove(tid);
                return;
            }
            target.damage(level * 0.4, attacker);
            target.addPotionEffect(new PotionEffect(slow, 30, level - 1, true, false));
            // Trigger BloodLust for nearby players
            com.factionenchants.enchantments.abilities.armor.BloodLust.triggerForNearby(target, plugin);
            ticks[0]++;
        }, 10L, 10L);
        taskMap.put(tid, task);
    }
}
