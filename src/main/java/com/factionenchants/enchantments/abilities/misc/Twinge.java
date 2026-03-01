package com.factionenchants.enchantments.abilities.misc;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Twinge extends CustomEnchantment {

    private final Random random = new Random();

    public Twinge() {
        super("twinge", "Twinge", 4, EnchantTier.LEGENDARY, ApplicableGear.TRIDENT);
    }

    @Override
    public String getDescription() {
        return "Make your enemy bleed if hit using melee attack.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 12) {
            // Simulate bleed via wither effect
            target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 + level * 20, 0));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, level - 1));
        }
    }
}
