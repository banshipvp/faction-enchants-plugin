package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Berserk extends CustomEnchantment {

    private final Random random = new Random();

    public Berserk() {
        super("berserk", "Berserk", 5, EnchantTier.UNIQUE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "A chance of strength and mining fatigue.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 8) {
            int dur = 100 + level * 20;
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, dur, level - 1));
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, dur, level - 1));
        }
    }
}
