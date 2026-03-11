package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Featherweight III — Sword enchantment, UNIQUE tier.
 * A chance to give a burst of Haste to the attacker on hit.
 */
public class Featherweight extends CustomEnchantment {

    private static final Random random = new Random();

    public Featherweight() {
        super("featherweight", "Featherweight", 3, EnchantTier.UNIQUE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "20% chance per level to gain Haste burst (2+level seconds, amplifier = level-1).";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 20% chance per level (20%, 40%, 60%)
        if (random.nextInt(100) < level * 20) {
            attacker.addPotionEffect(new PotionEffect(
                    PotionEffectType.FAST_DIGGING, 20 * (2 + level), level - 1, false, true));
        }
    }
}
