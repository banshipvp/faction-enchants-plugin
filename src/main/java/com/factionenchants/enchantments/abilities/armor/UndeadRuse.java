package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Undead Ruse X — Armor enchantment, ELITE tier.
 * When hit you have a chance to spawn zombie hordes to distract and disorient your opponents.
 */
public class UndeadRuse extends CustomEnchantment {

    private static final Random random = new Random();
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 20_000L;

    public UndeadRuse() {
        super("undead_ruse", "Undead Ruse", 10, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "When hit you have a chance to spawn zombie hordes to distract and disorient your opponents.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        UUID uid = defender.getUniqueId();
        long now = System.currentTimeMillis();
        if (now - cooldowns.getOrDefault(uid, 0L) < COOLDOWN_MS) return;

        // Chance: 5% per level (5%–50% at levels I–X)
        if (random.nextInt(100) >= level * 5) return;

        cooldowns.put(uid, now);

        // Spawn 1 zombie per 2 levels (1–5 zombies), targeted at attacker
        int zombieCount = Math.max(1, level / 2);
        for (int i = 0; i < zombieCount; i++) {
            Zombie zombie = (Zombie) defender.getWorld().spawnEntity(defender.getLocation(), EntityType.ZOMBIE);
            zombie.setCustomName("§5Undead Ruse");
            zombie.setCustomNameVisible(true);
            if (attacker instanceof Zombie) continue; // safety check
            if (attacker instanceof org.bukkit.entity.LivingEntity livingAttacker) {
                zombie.setTarget(livingAttacker);
            }
            // Disorient attacker: apply blindness + nausea
            if (attacker instanceof Player attackerPlayer) {
                attackerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, true));
                attackerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0, false, true));
            }
        }
        defender.sendMessage("§5[Undead Ruse] §fZombies rise to defend you!");
    }
}
