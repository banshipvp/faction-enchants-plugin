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

public class Ward extends CustomEnchantment {

    private final Random random = new Random();

    public Ward() {
        super("ward", "Ward", 4, EnchantTier.UNIQUE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "A chance to absorb enemy damage and heal you.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 8) {
            event.setDamage(0);
            double heal = 1.0 + level * 0.5;
            defender.setHealth(Math.min(defender.getHealth() + heal, defender.getMaxHealth()));
            defender.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 60, level - 1, true, false));
        }
    }
}
