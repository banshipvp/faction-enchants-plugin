package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Insomnia extends CustomEnchantment {

    private final Random random = new Random();

    public Insomnia() {
        super("insomnia", "Insomnia", 7, EnchantTier.SIMPLE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Gives slowness, slow swinging and confusion to the target.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 6) {
            int dur = 40 + level * 20;
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, dur, level - 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, dur, level - 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, dur, 0));
        }
    }
}
