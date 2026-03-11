package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Hi-jack IV — Bow enchantment, ELITE tier.
 * Chance to convert summoned enemy Guardians into your own when shot with an arrow.
 */
public class Hijack extends CustomEnchantment {

    private static final Random random = new Random();

    public Hijack() {
        super("hijack", "Hi-jack", 4, EnchantTier.ELITE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Chance to convert summoned enemy Guardians into your own when they are shot with an arrow.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Guardian guardian)) return;

        // 20% per level proc chance
        if (random.nextInt(100) >= level * 20) return;

        // Cancel the arrow damage — we are converting this guardian
        event.setCancelled(true);

        // Find the nearest enemy player (not the shooter) to redirect the guardian against
        List<Player> enemies = shooter.getNearbyEntities(64, 64, 64).stream()
                .filter(e -> e instanceof Player && !e.equals(shooter))
                .map(e -> (Player) e)
                .sorted((a, b) -> Double.compare(
                        a.getLocation().distanceSquared(guardian.getLocation()),
                        b.getLocation().distanceSquared(guardian.getLocation())))
                .collect(Collectors.toList());

        if (!enemies.isEmpty()) {
            guardian.setTarget(enemies.get(0));
        } else {
            guardian.setTarget(null);
        }
    }
}
