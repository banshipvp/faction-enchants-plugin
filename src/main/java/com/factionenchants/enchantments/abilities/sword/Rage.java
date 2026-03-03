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
        super("rage", "Rage", 5, EnchantTier.LEGENDARY,
                ApplicableGear.SWORD, ApplicableGear.AXE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Gain stacking strength with every hit.";
    }

    /** Applies stacking Strength on melee hits (sword / axe). */
    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        applyRage(attacker, level);
    }

    /** Applies stacking Strength on arrow hits (bow). */
    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        applyRage(shooter, level);
    }

    private void applyRage(Player player, int level) {
        int currentAmp = player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)
                ? player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() : -1;
        int newAmp = Math.min(currentAmp + 1, level);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, newAmp, true, true));
    }
}
