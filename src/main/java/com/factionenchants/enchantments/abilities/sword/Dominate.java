package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Dominate extends CustomEnchantment {

    public Dominate() {
        super("dominate", "Dominate", 4, EnchantTier.ULTIMATE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Applies Weakness to target (duration level×40 ticks, amplifier = level-1).";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, level * 40, level - 1, true, false));
    }
}
