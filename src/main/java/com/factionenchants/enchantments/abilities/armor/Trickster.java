package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Trickster VIII — Armor enchantment, ELITE tier.
 * When hit you have a chance to teleport directly behind your opponent and take them by surprise.
 */
public class Trickster extends CustomEnchantment {

    private static final Random random = new Random();
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 8_000L;

    public Trickster() {
        super("trickster", "Trickster", 8, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "When hit you have a chance to teleport directly behind your opponent and take them by surprise.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof Player attackerPlayer)) return;

        UUID uid = defender.getUniqueId();
        long now = System.currentTimeMillis();
        if (now - cooldowns.getOrDefault(uid, 0L) < COOLDOWN_MS) return;

        // Chance: 5% per level (5%–40% at levels I–VIII)
        if (random.nextInt(100) >= level * 5) return;

        cooldowns.put(uid, now);

        // Teleport defender directly behind the attacker
        Location behindAttacker = attackerPlayer.getLocation().clone();
        behindAttacker.add(behindAttacker.getDirection().normalize().multiply(-1.5));
        behindAttacker.setYaw(attackerPlayer.getLocation().getYaw() + 180);
        defender.teleport(behindAttacker);
        defender.sendMessage("§b[Trickster] §fYou teleported behind your attacker!");
    }
}
