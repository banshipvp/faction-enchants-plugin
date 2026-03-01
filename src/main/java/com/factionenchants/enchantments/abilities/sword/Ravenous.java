package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Ravenous extends CustomEnchantment {

    private final Random random = new Random();

    public Ravenous() {
        super("ravenous", "Ravenous", 4, EnchantTier.UNIQUE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Chance to regain hunger whilst in combat.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            int food = Math.min(20, attacker.getFoodLevel() + level);
            attacker.setFoodLevel(food);
            attacker.setSaturation(Math.min(attacker.getSaturation() + level, 20.0f));
        }
    }
}
