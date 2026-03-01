package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Confusion extends CustomEnchantment {

    private final Random random = new Random();

    public Confusion() {
        super("confusion", "Confusion", 3, EnchantTier.SIMPLE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Chance to deal nausea to your victim.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < (level * 12)) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80 + level * 20, 0));
        }
    }
}
