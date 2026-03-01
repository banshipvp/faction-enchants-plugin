package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class Ragdoll extends CustomEnchantment {

    private final Random random = new Random();

    public Ragdoll() {
        super("ragdoll", "Ragdoll", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Whenever you take damage you are pushed far back.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            Vector dir = defender.getLocation().subtract(attacker.getLocation()).toVector().normalize();
            dir.setY(0.4);
            defender.setVelocity(dir.multiply(1 + level * 0.4));
        }
    }
}
