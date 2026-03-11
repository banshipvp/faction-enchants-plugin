package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Venom III — Bow enchantment, ELITE tier.
 * A chance of dealing poison when your arrow hits.
 */
public class Venom extends CustomEnchantment {

    private static final Random random = new Random();

    public Venom() {
        super("venom", "Venom", 3, EnchantTier.ELITE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "A chance of dealing poison.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 25% chance per level (25%, 50%, 75%)
        if (random.nextInt(100) >= level * 25) return;

        // Poison amplifier: I = Poison I, II = Poison II, III = Poison III
        int amplifier = level - 1;
        int durationTicks = 60 + (level * 20); // 3–5 seconds
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, durationTicks, amplifier, false, true));
    }
}
