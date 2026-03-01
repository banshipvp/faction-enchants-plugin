package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Cripple extends CustomEnchantment {

    private final Random random = new Random();

    public Cripple() {
        super("cripple", "Cripple", 4, EnchantTier.UNIQUE, ApplicableGear.SWORD, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Slows enemies when you hit them.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < (level * 10)) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80 + level * 20, level - 1));
        }
    }
}
