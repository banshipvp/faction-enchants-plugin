package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class SmokeBomb extends CustomEnchantment {

    private final Random random = new Random();

    public SmokeBomb() {
        super("smoke_bomb", "Smoke Bomb", 8, EnchantTier.ELITE, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "When you are near death, you will spawn a smoke bomb to distract your enemies.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (defender.getHealth() - event.getFinalDamage() < 3.0 && random.nextInt(100) < level * 5) {
            Location loc = defender.getLocation();
            loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 5, 1, 1, 1, 0);
            loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 30, 2, 2, 2, 0.05);
            // Blind nearby enemies
            for (Entity e : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
                if (e instanceof Player p && !p.equals(defender)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                }
            }
        }
    }
}
