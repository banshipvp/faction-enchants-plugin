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
 * Voodoo VI — Armor enchantment, ELITE tier.
 * Gives a chance to deal weakness to attackers.
 */
public class Voodoo extends CustomEnchantment {

    private static final Random random = new Random();

    public Voodoo() {
        super("voodoo", "Voodoo", 6, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Gives a chance to deal weakness.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof LivingEntity livingAttacker)) return;

        // 10% chance per level (10%–60% at levels I–VI)
        if (random.nextInt(100) >= level * 10) return;

        // Weakness amplifier: I-III = amp 0 (Weakness I), IV-VI = amp 1 (Weakness II)
        int amplifier = level >= 4 ? 1 : 0;
        int durationTicks = 60 + (level * 15); // 3–6 seconds
        livingAttacker.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, durationTicks, amplifier, false, true));
    }
}
