package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Deep Wounds III — Sword enchantment, UNIQUE tier.
 * Increases the chance of inflicting the Bleed (wither) effect on the target.
 */
public class DeepWounds extends CustomEnchantment {

    private static final Random random = new Random();

    public DeepWounds() {
        super("deepwounds", "Deep Wounds", 3, EnchantTier.UNIQUE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Increases the chance of giving the bleed effect to your target.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 25% chance per level (25%, 50%, 75%)
        if (random.nextInt(100) < level * 25) {
            target.addPotionEffect(new PotionEffect(
                    PotionEffectType.WITHER, 20 * (2 + level), 0, false, true));
        }
    }
}
