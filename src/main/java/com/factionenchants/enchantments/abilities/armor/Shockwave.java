package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class Shockwave extends CustomEnchantment {

    private final Random random = new Random();

    public Shockwave() {
        super("shockwave", "Shockwave", 5, EnchantTier.ELITE, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "The chance to push back your attacker when your health is low.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (defender.getHealth() < defender.getMaxHealth() * 0.4 && random.nextInt(100) < level * 10) {
            Vector dir = attacker.getLocation().subtract(defender.getLocation()).toVector().normalize();
            dir.setY(0.4);
            attacker.setVelocity(dir.multiply(1.5 + level * 0.3));
        }
    }
}
