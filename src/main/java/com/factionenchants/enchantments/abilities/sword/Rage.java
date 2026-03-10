package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rage VI — Weapon enchantment (LEGENDARY).
 * For every consecutive combo hit the player lands, outgoing damage is multiplied
 * by 1.1× (stacking). A "combo" resets if the player goes 3 seconds without landing
 * a hit. The multiplier is capped to prevent runaway stacking.
 *
 * Stack cap: level * 5 hits (e.g. Rage VI allows up to 30 stacks = 3.0×).
 */
public class Rage extends CustomEnchantment {

    private static final long COMBO_RESET_MS = 3000L;
    private static final double PER_HIT_MULT = 1.10;

    /** player UUID -> combo hit count */
    private static final Map<UUID, Integer> combos = new ConcurrentHashMap<>();
    /** player UUID -> timestamp of last hit */
    private static final Map<UUID, Long> lastHit = new ConcurrentHashMap<>();

    public Rage() {
        super("rage", "Rage", 6, EnchantTier.LEGENDARY, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "For every combo hit you land, your damage is multiplied by 1.1×.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        UUID id = attacker.getUniqueId();
        long now = System.currentTimeMillis();

        // Reset combo if too much time passed since last hit
        if (now - lastHit.getOrDefault(id, 0L) > COMBO_RESET_MS) {
            combos.put(id, 0);
        }
        lastHit.put(id, now);

        int stack = combos.getOrDefault(id, 0) + 1;
        int cap = level * 5;
        stack = Math.min(stack, cap);
        combos.put(id, stack);

        // Apply multiplier: PER_HIT_MULT ^ stack
        double multiplier = Math.pow(PER_HIT_MULT, stack);
        event.setDamage(event.getDamage() * multiplier);
    }
}
