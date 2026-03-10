package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Destruction V — Helmet enchantment (LEGENDARY).
 * Automatically damages and debuffs all nearby enemies on a cooldown.
 * Radius: 5 blocks. Damage per proc: level * 0.5 hearts. Cooldown: 60 ticks per player.
 */
public class Destruction extends CustomEnchantment {

    private static final double RADIUS = 5.0;
    /** Tracks the last tick a player's Destruction proc'd to enforce cooldown. */
    private static final Map<UUID, Long> lastProc = new HashMap<>();
    /** Cooldown in milliseconds (3 seconds). */
    private static final long COOLDOWN_MS = 3000L;

    public Destruction() {
        super("destruction", "Destruction", 5, EnchantTier.LEGENDARY, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Automatically damages and debuffs all nearby enemies.";
    }

    @Override
    public void onTickPassive(Player player, int level, org.bukkit.inventory.ItemStack equipment) {
        long now = System.currentTimeMillis();
        UUID id = player.getUniqueId();
        if (now - lastProc.getOrDefault(id, 0L) < COOLDOWN_MS) return;
        lastProc.put(id, now);

        double damage = level * 0.5;
        int weaknessDuration = level * 40; // up to 5 * 40 = 200 ticks

        for (Entity nearby : player.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (!(nearby instanceof LivingEntity target)) continue;
            if (nearby instanceof Player nearbyPlayer && nearbyPlayer.equals(player)) continue;
            if (nearby.equals(player)) continue;

            target.damage(damage, player);
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, weaknessDuration, 0, false, true, true));
        }
    }
}
