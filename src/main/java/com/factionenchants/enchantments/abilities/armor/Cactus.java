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

public class Cactus extends CustomEnchantment {

    private final Random random = new Random();

    public Cactus() {
        super("cactus", "Cactus", 2, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Injures your attacker but does not affect your durability.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof LivingEntity le)) return;
        if (random.nextInt(100) < 40 + level * 15) {
            le.damage(level * 1.5, defender);
        }
    }
}
