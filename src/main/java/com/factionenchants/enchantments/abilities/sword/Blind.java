package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Blind III — Sword enchantment, ELITE tier.
 * A chance of causing blindness when attacking.
 */
public class Blind extends CustomEnchantment {

    private static final Random random = new Random();

    public Blind() {
        super("blind", "Blind", 3, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "15% chance per level to blind target for 3-9 seconds (scales with level).";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 15% per level proc chance
        if (random.nextInt(100) < level * 15) {
            target.addPotionEffect(new PotionEffect(
                    PotionEffectType.BLINDNESS, 60 + level * 20, 0, false, true));
        }
    }
}
