package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Paralyze IV — Sword enchantment, ELITE tier.
 * Gives a lightning effect with a chance to apply slowness and slow swinging,
 * and inflicts extra direct damage on proc.
 */
public class Paralyze extends CustomEnchantment {

    private static final Random random = new Random();

    public Paralyze() {
        super("paralyze", "Paralyze", 4, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Gives lightning effect and a chance for slowness and slow swinging. Also inflicts direct damage on proc.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 15% per level proc chance
        if (random.nextInt(100) >= level * 15) return;

        // Visual-only lightning strike (no fire, no damage from the bolt)
        target.getWorld().strikeLightningEffect(target.getLocation());

        // Apply slowness and mining fatigue (slow swinging)
        int duration = 40 + level * 20;
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, level - 1, false, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, level - 1, false, true));

        // Inflict bonus direct damage
        event.setDamage(event.getDamage() + level * 1.5);
    }
}
