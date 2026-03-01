package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Epicness extends CustomEnchantment {

    private final Random random = new Random();

    public Epicness() {
        super("epicness", "Epicness", 3, EnchantTier.SIMPLE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Gives particles and sound effects.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 15) {
            target.getWorld().spawnParticle(Particle.CRIT_MAGIC, target.getLocation().add(0, 1, 0), 15, 0.3, 0.3, 0.3, 0.1);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.4f, 1.5f + level * 0.2f);
        }
    }
}
