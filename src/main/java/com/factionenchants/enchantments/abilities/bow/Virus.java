package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Virus extends CustomEnchantment {

    private final Random random = new Random();

    public Virus() {
        super("virus", "Virus", 4, EnchantTier.UNIQUE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Multiplies all Wither and Poison damage the affected target receives and has a chance to remove regeneration effects on hit.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Apply wither - simulated virus effect
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60 + level * 20, level - 1));
        if (random.nextInt(100) < level * 12) {
            target.removePotionEffect(PotionEffectType.REGENERATION);
        }
    }
}
