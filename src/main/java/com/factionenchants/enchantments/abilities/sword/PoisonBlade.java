package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PoisonBlade extends CustomEnchantment {

    private final Random random = new Random();

    public PoisonBlade() {
        super("poison_blade", "Poison", 3, EnchantTier.ELITE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "A chance of giving poison effect.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < (level * 12)) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60 + level * 20, level - 1));
        }
    }
}
