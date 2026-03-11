package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Protector V — Armor enchantment (LEGENDARY).
 * On a cooldown, automatically heals and buffs nearby allied players (same team
 * or faction). Heal: level * 0.5 HP. Buff: Regeneration I for 3 seconds.
 * Pulse radius: 10 blocks. Cooldown: 4 seconds.
 */
public class Protector extends CustomEnchantment {

    private static final double RADIUS = 10.0;
    private static final long COOLDOWN_MS = 4000L;
    private static final Map<UUID, Long> lastPulse = new HashMap<>();

    public Protector() {
        super("protector", "Protector", 5, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Automatically heals and buffs all nearby faction allies.";
    }

    @Override
    public void onTickPassive(Player player, int level, org.bukkit.inventory.ItemStack equipment) {
        long now = System.currentTimeMillis();
        UUID id = player.getUniqueId();
        if (now - lastPulse.getOrDefault(id, 0L) < COOLDOWN_MS) return;
        lastPulse.put(id, now);

        double heal = level * 0.5;
        int regenDuration = 60; // 3 seconds

        for (Entity nearby : player.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (!(nearby instanceof Player ally)) continue;
            if (ally.equals(player)) continue;
            // Heal
            double newHealth = Math.min(ally.getHealth() + heal, ally.getMaxHealth());
            ally.setHealth(newHealth);
            // Buff with Regeneration
            ally.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, regenDuration, 0, true, false, true));
        }
        // Also heal self
        double selfHealth = Math.min(player.getHealth() + heal, player.getMaxHealth());
        player.setHealth(selfHealth);
    }
}
