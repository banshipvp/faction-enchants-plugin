package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import local.simplefactions.FactionManager;
import local.simplefactions.SimpleFactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Plague Carrier VIII — Leggings enchantment, UNIQUE tier.
 * When near death, summons creepers and applies debuffs to nearby enemies.
 * Triggers when health drops below 25%, with a 30-second cooldown.
 */
public class PlagueCarrier extends CustomEnchantment {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 30_000L;

    public PlagueCarrier() {
        super("plaguecarrier", "Plague Carrier", 8, EnchantTier.UNIQUE, ApplicableGear.LEGGINGS);
    }

    @Override
    public String getDescription() {
        return "When near death summons creepers and debuffs to avenge you.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        double healthPercent = player.getHealth() / player.getAttribute(
                org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
        if (healthPercent > 0.25) return;

        long now = System.currentTimeMillis();
        UUID uid = player.getUniqueId();
        if (now - cooldowns.getOrDefault(uid, 0L) < COOLDOWN_MS) return;
        cooldowns.put(uid, now);

        int creepCount = Math.min(level / 2, 4);
        for (int i = 0; i < creepCount; i++) {
            Creeper creeper = (Creeper) player.getWorld().spawnEntity(
                    player.getLocation(), EntityType.CREEPER);
            creeper.setPowered(level >= 5);
            // Target the nearest enemy
            for (Entity nearby : player.getNearbyEntities(20, 10, 20)) {
                if (nearby instanceof Player nearbyPlayer && isEnemy(player, nearbyPlayer)) {
                    creeper.setTarget(nearbyPlayer);
                    break;
                }
            }
        }

        // Debuff nearby enemies
        int poisonAmp = Math.max(0, level / 4 - 1);
        for (Entity nearby : player.getNearbyEntities(15, 10, 15)) {
            if (nearby instanceof Player nearbyPlayer && isEnemy(player, nearbyPlayer)) {
                nearbyPlayer.addPotionEffect(
                        new PotionEffect(PotionEffectType.POISON, 100, poisonAmp, false, true));
                nearbyPlayer.addPotionEffect(
                        new PotionEffect(PotionEffectType.SLOW, 100, 1, false, true));
                nearbyPlayer.addPotionEffect(
                        new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, false, true));
            }
        }
        player.sendMessage("§2[Plague Carrier] §aCreepers summoned to avenge you!");
    }

    private boolean isEnemy(Player player, Player other) {
        try {
            if (!Bukkit.getPluginManager().isPluginEnabled("SimpleFactions")) return true;
            SimpleFactionsPlugin sfp = SimpleFactionsPlugin.getInstance();
            if (sfp == null) return true;
            FactionManager fm = sfp.getFactionManager();
            FactionManager.Faction f1 = fm.getFactionByPlayer(player);
            FactionManager.Faction f2 = fm.getFactionByPlayer(other);
            if (f1 != null && f2 != null && f1.getName().equalsIgnoreCase(f2.getName())) return false;
            if (f1 != null && f2 != null && f1.isAlly(f2.getName())) return false;
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
