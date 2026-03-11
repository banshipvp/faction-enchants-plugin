package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import local.simplefactions.FactionManager;
import local.simplefactions.SimpleFactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Spirit Link V — Chestplate enchantment, ELITE tier.
 * Heals nearby faction/allies when you are damaged.
 */
public class SpiritLink extends CustomEnchantment {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 5_000L;

    public SpiritLink() {
        super("spiritlink", "Spirit Link", 5, EnchantTier.ELITE, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "Heals nearby faction/allies when you are damaged.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        UUID uid = defender.getUniqueId();
        long now = System.currentTimeMillis();
        if (now - cooldowns.getOrDefault(uid, 0L) < COOLDOWN_MS) return;
        cooldowns.put(uid, now);

        // Heal per level: 0.5 half-hearts (0.25 HP) per level
        double healAmount = level * 0.5;

        for (Entity nearby : defender.getNearbyEntities(15, 10, 15)) {
            if (!(nearby instanceof Player nearbyPlayer)) continue;
            if (nearbyPlayer.equals(defender)) continue;
            if (!isAlly(defender, nearbyPlayer)) continue;

            double newHp = Math.min(nearbyPlayer.getHealth() + healAmount,
                    nearbyPlayer.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
            nearbyPlayer.setHealth(newHp);
        }
    }

    private boolean isAlly(Player player, Player other) {
        try {
            if (!Bukkit.getPluginManager().isPluginEnabled("SimpleFactions")) return false;
            SimpleFactionsPlugin sfp = SimpleFactionsPlugin.getInstance();
            if (sfp == null) return false;
            FactionManager fm = sfp.getFactionManager();
            FactionManager.Faction f1 = fm.getFactionByPlayer(player);
            FactionManager.Faction f2 = fm.getFactionByPlayer(other);
            if (f1 == null || f2 == null) return false;
            if (f1.getName().equalsIgnoreCase(f2.getName())) return true;
            if (f1.isAlly(f2.getName())) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
