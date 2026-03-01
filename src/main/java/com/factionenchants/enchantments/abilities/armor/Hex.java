package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Hex extends CustomEnchantment {

    private final Random random = new Random();

    public Hex() {
        super("hex", "Hex", 5, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Chance to curse your attacker with multiple potion effects.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 8 && attacker instanceof LivingEntity le) {
            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, level * 60, level - 1));
            le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, level * 60, level - 1));
            le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, level * 40, 0));
        }
    }
}
