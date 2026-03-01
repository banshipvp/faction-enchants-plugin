package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Poltergeist extends CustomEnchantment {

    private final Random random = new Random();

    public Poltergeist() {
        super("poltergeist", "Poltergeist", 5, EnchantTier.MASTERY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Throws attackers into the air and damages them.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10 && attacker instanceof org.bukkit.entity.LivingEntity le) {
            le.setVelocity(new org.bukkit.util.Vector(0, 3 + level * 0.5, 0));
            le.damage(level * 2.0, defender);
        }
    }
}
