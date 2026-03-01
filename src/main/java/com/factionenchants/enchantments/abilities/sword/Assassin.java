package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Assassin extends CustomEnchantment {

    private final Random random = new Random();

    public Assassin() {
        super("assassin", "Assassin", 5, EnchantTier.ULTIMATE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "The closer you are to your enemy, the more damage you deal (up to 1.25x). If more than 2 blocks away, you deal LESS damage.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double dist = attacker.getLocation().distance(target.getLocation());
        double multiplier;
        if (dist <= 2.0) {
            multiplier = 1.0 + (level * 0.05);
        } else {
            multiplier = Math.max(0.6, 1.0 - (dist - 2.0) * 0.05);
        }
        event.setDamage(event.getDamage() * multiplier);
    }
}
