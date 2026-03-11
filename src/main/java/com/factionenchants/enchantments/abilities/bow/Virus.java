package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Virus IV — Bow enchantment, UNIQUE tier.
 * Infects the target: all Wither and Poison damage they receive is multiplied,
 * and there is a chance to strip their regeneration on hit.
 * The infection lasts 30 seconds. Damage multiplier is handled by VirusEffectListener.
 */
public class Virus extends CustomEnchantment {

    private static final Random random = new Random();

    /** UUID -> {level, expiryEpochMs}. Read by VirusEffectListener. */
    public static final Map<UUID, long[]> infected = new HashMap<>();

    public Virus() {
        super("virus", "Virus", 4, EnchantTier.UNIQUE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Multiplies all Wither and Poison damage the affected target receives and has a chance to remove regeneration effects on hit.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Mark target as infected (30-second window)
        infected.put(target.getUniqueId(), new long[]{level, System.currentTimeMillis() + 30_000L});

        // Apply initial wither and poison
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 0, false, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0, false, true));

        // Chance to remove regeneration: 25% per level
        if (random.nextInt(100) < level * 25) {
            target.removePotionEffect(PotionEffectType.REGENERATION);
            if (target instanceof Player tp) {
                tp.sendMessage("§4[Virus] §cYour regeneration has been stripped!");
            }
        }
    }
}
