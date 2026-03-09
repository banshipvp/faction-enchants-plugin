package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rage — Weapon (sword/axe/bow) enchantment.
 * Tracks consecutive "combo" hits. For every hit you land in succession
 * (within 3.5 seconds of the last hit) your outgoing damage is multiplied by an extra 1.1x.
 * Combo resets after 3.5 seconds of no hits or on death.
 */
public class Rage extends CustomEnchantment {

    /** Maps attacker UUID → [comboCount, lastHitMs]. */
    private static final Map<UUID, long[]> comboData = new ConcurrentHashMap<>();
    private static final long WINDOW_MS = 3500L;

    public Rage() {
        super("rage", "Rage", 6, EnchantTier.LEGENDARY,
                ApplicableGear.SWORD, ApplicableGear.AXE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "For every combo hit you land your damage is multiplied by 1.1x. Combo resets after 3.5 seconds.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        applyRage(attacker, level, event);
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        applyRage(shooter, level, event);
    }

    private void applyRage(Player player, int level, EntityDamageByEntityEvent event) {
        UUID id = player.getUniqueId();
        long now = System.currentTimeMillis();
        long[] data = comboData.getOrDefault(id, new long[]{0L, 0L});
        long combo = (now - data[1] <= WINDOW_MS) ? Math.min(data[0] + 1, level * 2L) : 1L;
        comboData.put(id, new long[]{combo, now});
        // Each combo hit multiplies damage by 1.1
        event.setDamage(event.getDamage() * Math.pow(1.1, combo));
    }
}
