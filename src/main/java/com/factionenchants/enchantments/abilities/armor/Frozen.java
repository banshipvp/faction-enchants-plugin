package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Frozen extends CustomEnchantment {

    private final Random random = new Random();

    public Frozen() {
        super("frozen", "Frozen", 3, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Can cause slowness to attacker when defending.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof LivingEntity le)) return;
        if (random.nextInt(100) < level * 15) {
            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 + level * 20, level - 1));
        }
    }
}
