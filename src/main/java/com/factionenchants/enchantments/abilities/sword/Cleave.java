package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Cleave extends CustomEnchantment {

    private final Random random = new Random();

    public Cleave() {
        super("cleave", "Cleave", 7, EnchantTier.ULTIMATE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "8% chance per level to damage nearby enemies (radius 1+level×0.5, damage = level).";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 8) {
            double radius = 1 + level * 0.5;
            double dmg = level * 1.0;
            for (Entity e : target.getWorld().getNearbyEntities(target.getLocation(), radius, radius, radius)) {
                if (e instanceof LivingEntity le && !e.equals(target) && !e.equals(attacker)) {
                    le.damage(dmg, attacker);
                }
            }
        }
    }
}
