package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Self Destruct III — Armor enchantment, UNIQUE tier.
 * When close to death, buffed explosions spawn around you to damage nearby enemies.
 * Triggers when health drops below 25%, with a 20-second cooldown.
 */
public class SelfDestruct extends CustomEnchantment {

    private static final Random random = new Random();
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 20_000L;

    public SelfDestruct() {
        super("selfdestruct", "Self Destruct", 3, EnchantTier.UNIQUE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "When close to death buffed TnT spawns around you.";
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

        int blasts = level + 1;
        for (int i = 0; i < blasts; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double dist = 4.0 + random.nextDouble() * 4.0;
            org.bukkit.Location expLoc = player.getLocation().clone()
                    .add(Math.cos(angle) * dist, 0.5, Math.sin(angle) * dist);
            // false, false = no fire, no block damage — keeps it purely combat-damage
            player.getWorld().createExplosion(expLoc, 4.0f + level, false, false);
        }
        player.sendMessage("§c[Self Destruct] §eExplosions triggered — fight back!");
    }
}
