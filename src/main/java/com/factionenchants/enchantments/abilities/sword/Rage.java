package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Rage extends CustomEnchantment {

    public Rage() {
        super("rage", "Rage", 5, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Gain stacking strength with every hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        int currentAmp = attacker.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)
                ? attacker.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() : -1;
        int newAmp = Math.min(currentAmp + 1, level);
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, newAmp, true, true));
    }
}
