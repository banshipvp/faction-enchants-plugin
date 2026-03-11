package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.abilities.armor.BloodLust;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Bleed – AXE enchantment, ULTIMATE tier, max level VI.
 * On hit, has a level-scaled chance to apply a bleeding DoT that deals
 * damage every second for (level + 2) ticks. Each tick also calls
 * {@link BloodLust#triggerForNearby} so nearby BloodLust wearers can heal.
 */
public class Bleed extends CustomEnchantment {

    /** UUIDs of entities currently bleeding – read by BloodLust. */
    public static final Set<UUID> BLEED_VICTIMS = new HashSet<>();

    private static final Map<UUID, Integer> taskMap = new HashMap<>();
    private static final Random random = new Random();

    public Bleed() {
        super("bleed", "Bleed", 6, EnchantTier.ULTIMATE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Applies bleed stacks to enemies that decrease their movement speed. " +
               "Use in combination with Devour and Blood Lust.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level,
                            EntityDamageByEntityEvent event) {
        // Chance: 10% per level (10% – 60%)
        if (random.nextInt(100) >= level * 10) return;

        Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;

        UUID tid = target.getUniqueId();

        // Cancel any existing bleed task on this target and reset it
        Integer oldTask = taskMap.get(tid);
        if (oldTask != null) Bukkit.getScheduler().cancelTask(oldTask);

        BLEED_VICTIMS.add(tid);

        int maxTicks = level + 2; // level 1 → 3 ticks, level 6 → 8 ticks
        int[] ticks = {0};

        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!target.isValid() || target.isDead() || ticks[0] >= maxTicks) {
                Bukkit.getScheduler().cancelTask(taskMap.getOrDefault(tid, -1));
                taskMap.remove(tid);
                BLEED_VICTIMS.remove(tid);
                return;
            }
            // Deal bleed damage (bypasses armour via indirect attacker reference)
            target.damage(level * 0.5, attacker);
            // Apply slowness stack based on bleed level
            int slownessAmplifier = Math.min(level - 1, 3); // amplifier 0–3 (Slowness I–IV)
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 25, slownessAmplifier, false, false), true);
            // Notify BloodLust wearers nearby
            BloodLust.triggerForNearby(target, plugin);
            ticks[0]++;
        }, 20L, 20L);

        taskMap.put(tid, task);
    }
}
