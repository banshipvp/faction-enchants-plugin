package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Snare extends CustomEnchantment {

    private final Random random = new Random();

    public Snare() {
        super("snare", "Snare", 4, EnchantTier.ELITE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Chance to slow and fatigue enemies with projectiles.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 12) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 + level * 20, level - 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60 + level * 20, level - 1));
        }
    }
}
