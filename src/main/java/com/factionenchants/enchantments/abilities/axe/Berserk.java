package com.factionenchants.enchantments.abilities.axe;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Berserk V — Axe enchantment, UNIQUE tier.
 * A chance to grant Strength to the attacker and apply Mining Fatigue to the target.
 */
public class Berserk extends CustomEnchantment {

    private static final Random random = new Random();

    public Berserk() {
        super("berserk", "Berserk", 5, EnchantTier.UNIQUE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "A chance to give yourself Strength and the target Mining Fatigue.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 15% chance per level (15%–75%)
        if (random.nextInt(100) < level * 15) {
            int duration = 20 * (3 + level);
            attacker.addPotionEffect(new PotionEffect(
                    PotionEffectType.INCREASE_DAMAGE, duration, level / 2, false, true));
            target.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW_DIGGING, duration, level / 2, false, true));
        }
    }
}
