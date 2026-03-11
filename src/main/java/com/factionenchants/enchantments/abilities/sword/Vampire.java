package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Vampire III — Sword enchantment, ELITE tier.
 * A chance to heal you for up to 3hp a few seconds after you strike.
 */
public class Vampire extends CustomEnchantment {

    private static final Random random = new Random();

    public Vampire() {
        super("vampire", "Vampire", 3, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "25% chance per level to heal 1 HP per level, 3 seconds after striking.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 25% chance per level (25%, 50%, 75%)
        if (random.nextInt(100) >= level * 25) return;

        // Delayed heal: 1 hp per level, applied 3 seconds later
        double healAmount = level * 1.0;
        Bukkit.getScheduler().runTaskLater(FactionEnchantsPlugin.getInstance(), () -> {
            if (!attacker.isOnline() || attacker.isDead()) return;
            double newHp = Math.min(attacker.getHealth() + healAmount,
                    attacker.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
            attacker.setHealth(newHp);
        }, 60L); // 3 seconds (60 ticks)
    }
}
