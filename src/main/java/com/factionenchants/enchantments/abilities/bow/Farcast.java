package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class Farcast extends CustomEnchantment {

    private final Random random = new Random();

    public Farcast() {
        super("farcast", "Farcast", 5, EnchantTier.ELITE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Chance to knockback melee attackers by a couple of blocks when they hit you. The lower your health, the higher the chance to proc.";
    }

    @Override
    public void onHurtBy(org.bukkit.entity.Player defender, org.bukkit.entity.Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (attacker instanceof Player) return; // Only for melee, handled differently
        double hpRatio = 1 - (defender.getHealth() / defender.getMaxHealth());
        double chance = (level * 8 + hpRatio * 20);
        if (random.nextDouble() * 100 < chance) {
            Vector dir = attacker.getLocation().subtract(defender.getLocation()).toVector().normalize();
            dir.setY(0.3);
            attacker.setVelocity(dir.multiply(1.5 + level * 0.4));
        }
    }
}
