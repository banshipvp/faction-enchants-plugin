package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Trap extends CustomEnchantment {

    private final Random random = new Random();

    public Trap() {
        super("trap", "Trap", 3, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Chance to give buffed slowness effect.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 + level * 20, 2 + level));
        }
    }
}
