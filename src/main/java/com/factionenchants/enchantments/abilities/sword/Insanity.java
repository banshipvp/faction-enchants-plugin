package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Insanity extends CustomEnchantment {

    private final Random random = new Random();

    public Insanity() {
        super("insanity", "Insanity", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Drives enemies insane with random negative effects.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, level * 60, 0));
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, level * 40, 0));
        if (random.nextBoolean()) target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, level * 40, level - 1));
        if (random.nextBoolean()) target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, level * 40, 0));
    }
}
