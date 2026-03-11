package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Wither V — Armor enchantment, ELITE tier.
 * A chance to give the wither effect to attackers.
 */
public class WitherEnchant extends CustomEnchantment {

    private static final Random random = new Random();

    public WitherEnchant() {
        super("wither", "Wither", 5, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "A chance to give the wither effect.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof LivingEntity livingAttacker)) return;

        // 10% chance per level (10%–50% at levels I–V)
        if (random.nextInt(100) >= level * 10) return;

        // Wither amplifier scales: I-II = amp 0, III-IV = amp 1, V = amp 2
        int amplifier = Math.max(0, (level - 1) / 2);
        int durationTicks = 60 + (level * 20); // 3–7 seconds
        livingAttacker.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, durationTicks, amplifier, false, true));
    }
}
