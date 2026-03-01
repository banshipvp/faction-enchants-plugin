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

public class Poisoned extends CustomEnchantment {

    private final Random random = new Random();

    public Poisoned() {
        super("poisoned", "Poisoned", 4, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Chance to give poison to your attacker.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof LivingEntity le)) return;
        if (random.nextInt(100) < level * 12) {
            le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60 + level * 20, level - 1));
        }
    }
}
