package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class Obliterate extends CustomEnchantment {

    private final Random random = new Random();

    public Obliterate() {
        super("obliterate", "Obliterate", 5, EnchantTier.SIMPLE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Extreme knockback.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            Vector dir = target.getLocation().subtract(attacker.getLocation()).toVector().normalize();
            dir.setY(0.3 + level * 0.1);
            target.setVelocity(dir.multiply(1.2 + level * 0.4));
        }
    }
}
