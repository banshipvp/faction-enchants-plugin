package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Solitude III — Sword enchantment, ELITE tier.
 * Increases chance and length of causing a silence-like effect on enemy players by up to 3% per level.
 */
public class Solitude extends CustomEnchantment {

    private static final Random random = new Random();

    public Solitude() {
        super("solitude", "Solitude", 3, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Increases chance and length of the Silence enchantment procing on enemy players by up to 3%.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player)) return;

        // 10% per level base chance; each level also adds 3% bonus per the description
        int chance = level * 10 + level * 3;
        if (random.nextInt(100) >= chance) return;

        // Apply a silence-like effect: blindness + nausea to simulate being silenced
        int duration = 30 + level * 20;
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0, false, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration, 0, false, true));
    }
}
