package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Sabotage extends CustomEnchantment {

    private final Random random = new Random();

    /** UUID → expiry time in ms. Sabotaged players cannot use Rocket/Guided Rocket Escape. */
    private static final Map<UUID, Long> sabotaged = new HashMap<>();
    private static final long DURATION_MS = 5_000L; // 5 seconds per proc

    public Sabotage() {
        super("sabotage", "Sabotage", 5, EnchantTier.SOUL, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Active Soul Enchantment. A chance to block enemy player Rocket Escape and Guided Rocket Escape from activating. Costs 4 souls per second.";
    }

    @Override
    public int getSoulCostPerProc() {
        return 4;
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;

        // Chance to proc: level * 15%
        int chance = level * 15;
        if (random.nextInt(100) >= chance) return;

        sabotaged.put(victim.getUniqueId(), System.currentTimeMillis() + DURATION_MS);
        victim.sendMessage("\u00a7c\u2736 Your escape abilities have been Sabotaged!");
        victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_CHAIN_BREAK, 1.0f, 1.0f);
        attacker.sendMessage("\u00a7a\u2736 Sabotage activated on " + victim.getName() + "!");
    }

    /** Check whether a player's escape abilities are currently sabotaged. */
    public static boolean isSabotaged(UUID uuid) {
        Long expiry = sabotaged.get(uuid);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            sabotaged.remove(uuid);
            return false;
        }
        return true;
    }
}
