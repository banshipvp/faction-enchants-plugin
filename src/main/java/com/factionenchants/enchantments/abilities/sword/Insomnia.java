package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Insomnia VII — Sword enchantment, UNIQUE tier.
 * Gives slowness, slow swinging and confusion to struck enemies.
 */
public class Insomnia extends CustomEnchantment {

    public Insomnia() {
        super("insomnia", "Insomnia", 7, EnchantTier.UNIQUE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Gives slowness, slow swinging and confusion.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        int duration = 30 + level * 15; // scales 45–135 ticks (2.25–6.75 s) across levels I–VII
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,       duration, level - 1,  false, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, 0,          false, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,   duration, 0,          false, true));
    }
}
