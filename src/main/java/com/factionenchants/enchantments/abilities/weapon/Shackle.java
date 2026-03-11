package com.factionenchants.enchantments.abilities.weapon;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.listeners.SpawnerMobTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

/**
 * Shackle III — Weapon enchantment, ELITE tier.
 * Prevents mobs spawned from mob spawners from suffering knockback from your attacks.
 */
public class Shackle extends CustomEnchantment {

    public Shackle() {
        super("shackle", "Shackle", 3, EnchantTier.ELITE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Prevents mobs spawned from mob spawners from suffering from knockback from your attacks.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!SpawnerMobTracker.isSpawnerMob(target.getUniqueId())) return;

        // Schedule a 1-tick-later task to zero the entity's velocity, negating knockback
        Bukkit.getScheduler().runTaskLater(FactionEnchantsPlugin.getInstance(), () -> {
            if (target.isValid()) {
                target.setVelocity(new Vector(0, 0, 0));
            }
        }, 1L);
    }
}
