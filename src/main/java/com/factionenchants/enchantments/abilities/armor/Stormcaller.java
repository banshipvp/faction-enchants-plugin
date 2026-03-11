package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Stormcaller IV — Armor enchantment, ELITE tier.
 * Strikes lightning on attacking players when you are hit.
 */
public class Stormcaller extends CustomEnchantment {

    private static final Random random = new Random();
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 4_000L;

    public Stormcaller() {
        super("stormcaller", "Stormcaller", 4, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "15% chance per level to strike lightning on your attacker (4 second cooldown).";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof Player)) return;

        UUID uid = defender.getUniqueId();
        long now = System.currentTimeMillis();
        if (now - cooldowns.getOrDefault(uid, 0L) < COOLDOWN_MS) return;

        // 15% chance per level (15%–60% at levels I–IV)
        if (random.nextInt(100) >= level * 15) return;

        cooldowns.put(uid, now);
        attacker.getWorld().strikeLightning(attacker.getLocation());
    }
}
