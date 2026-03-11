package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Epicness III — Sword enchantment, SIMPLE tier.
 * Gives particles and sound effects when hitting an enemy.
 */
public class Epicness extends CustomEnchantment {

    private static final Random random = new Random();

    public Epicness() {
        super("epicness", "Epicness", 3, EnchantTier.SIMPLE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Gives particles and sound effects when striking an enemy.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        Location loc = target.getLocation().add(0, 1, 0);
        int particleCount = level * 8;
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, particleCount,
                0.3, 0.5, 0.3, new Particle.DustOptions(Color.fromRGB(255, 100 + level * 50, 0), 1.2f));
        Sound[] sounds = {Sound.ENTITY_LIGHTNING_BOLT_THUNDER, Sound.ENTITY_GENERIC_EXPLODE, Sound.BLOCK_ANVIL_LAND};
        Sound chosen = sounds[random.nextInt(sounds.length)];
        loc.getWorld().playSound(loc, chosen, 0.5f, 1.2f + level * 0.1f);
    }
}
