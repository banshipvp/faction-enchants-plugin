package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Pacify extends CustomEnchantment {

    public Pacify() {
        super("pacify", "Pacify", 4, EnchantTier.ULTIMATE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "An enchantment made to mess with people's minds.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, level * 60, 0, true, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, level * 20, 0, true, false));
    }
}
