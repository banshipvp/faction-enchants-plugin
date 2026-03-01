package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Sustain extends CustomEnchantment {

    private final Random random = new Random();

    public Sustain() {
        super("sustain", "Sustain", 4, EnchantTier.UNIQUE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Chance to regain hunger when getting hit.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            int newFood = Math.min(20, defender.getFoodLevel() + level);
            defender.setFoodLevel(newFood);
        }
    }
}
