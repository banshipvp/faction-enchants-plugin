package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Collections;
import java.util.HashSet;

/**
 * Kill Aura V — Sword enchantment (LEGENDARY).
 * On kill, there is a (level * 15)% chance for the death event to chain to all
 * nearby monsters within a 6-block radius. Chains are guarded to avoid recursion.
 * Radius: 6 blocks.
 */
public class KillAura extends CustomEnchantment {

    private static final double RADIUS = 6.0;
    /** Simple guard to avoid cascading chain kills. */
    private static final Set<UUID> PROCESSING = Collections.synchronizedSet(new HashSet<>());

    public KillAura() {
        super("kill_aura", "Kill Aura", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Chance to kill multiple monsters in a stack each death event.";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        if (PROCESSING.contains(killer.getUniqueId())) return;

        double procChance = level * 0.15;
        if (Math.random() >= procChance) return;

        PROCESSING.add(killer.getUniqueId());
        try {
            List<Entity> nearby = victim.getNearbyEntities(RADIUS, RADIUS, RADIUS);
            for (Entity e : nearby) {
                if (!(e instanceof Monster mob)) continue;
                if (mob.isDead()) continue;
                mob.damage(mob.getHealth() * 2, killer); // Overkill to ensure death
            }
        } finally {
            PROCESSING.remove(killer.getUniqueId());
        }
    }
}
