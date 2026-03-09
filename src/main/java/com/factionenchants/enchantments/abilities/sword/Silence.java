package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Silence — Sword enchantment.
 * A chance to silence the target for a brief period, suppressing their custom defensive
 * armour enchants (onHurtBy). Checked in CombatListener before firing defender enchants.
 */
public class Silence extends CustomEnchantment {

    /** Maps silenced defender UUID → expiry timestamp (ms). */
    public static final Map<UUID, Long> SILENCED = new ConcurrentHashMap<>();

    private final Random random = new Random();

    public Silence() {
        super("silence", "Silence", 4, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Chance to silence the target, suppressing their custom defensive enchantments for up to 2 seconds.";
    }

    public static boolean isSilenced(UUID playerId) {
        Long expiry = SILENCED.get(playerId);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) { SILENCED.remove(playerId); return false; }
        return true;
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Proc chance: 15% per level (15–60%)
        if (random.nextInt(100) >= level * 15) return;
        // Duration: 1–2 seconds scaling with level
        long durationMs = 1000L + level * 250L;
        SILENCED.put(target.getUniqueId(), System.currentTimeMillis() + durationMs);
        if (target instanceof Player p) {
            p.sendMessage("\u00a7cYou have been Silenced!");
        }
    }
}
