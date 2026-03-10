package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Unfocus extends CustomEnchantment {

    private final Random random = new Random();
    // Tracks unfocused players and when the effect expires (ms timestamp)
    public static final Map<UUID, Long> UNFOCUSED = new ConcurrentHashMap<>();
    /** Damage reduction applied to unfocused players' outgoing bow shots (50%). */
    public static final double REDUCTION = 0.50;

    public Unfocus() {
        super("unfocus", "Unfocus", 5, EnchantTier.ULTIMATE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Chance to Unfocus target player, reducing their outgoing bow damage by 50% for up to 10 seconds.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player targetPlayer)) return;
        // Chance: level * 12%
        if (random.nextInt(100) >= level * 12) return;

        long durationMs = (long) (level * 2000L); // 2s per level, up to 10s at V
        UNFOCUSED.put(targetPlayer.getUniqueId(), System.currentTimeMillis() + durationMs);
        targetPlayer.sendMessage("\u00a7cYou have been Unfocused! Your bow damage is reduced for " + (durationMs / 1000) + "s.");
    }

    /** Called by CombatListener when an unfocused player fires an arrow to reduce its damage. */
    public static boolean isUnfocused(UUID uuid) {
        Long expiry = UNFOCUSED.get(uuid);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            UNFOCUSED.remove(uuid);
            return false;
        }
        return true;
    }
}
