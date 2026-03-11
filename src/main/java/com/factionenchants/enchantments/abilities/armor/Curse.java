package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Curse V — Chestplate enchantment, UNIQUE tier.
 * Gives Strength, Slowness, and Resistance when the wearer drops below 40% HP.
 */
public class Curse extends CustomEnchantment {

    public Curse() {
        super("curse", "Curse", 5, EnchantTier.UNIQUE, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "Gives strength, slowness, and resistance when you drop below 40% HP.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double hpPercent = defender.getHealth() / defender.getMaxHealth();
        if (hpPercent <= 0.40) {
            int duration = 20 * (3 + level);
            defender.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, level - 1, false, true));
            defender.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, level / 2, false, true));
            defender.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, level / 3, false, true));
        }
    }
}
