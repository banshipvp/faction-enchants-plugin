package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Piercing extends CustomEnchantment {

    public Piercing() {
        super("piercing", "Piercing", 5, EnchantTier.ULTIMATE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Inflicts 10% more damage per level (max 50% at level V).";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // +10% damage per level
        event.setDamage(event.getDamage() * (1.0 + level * 0.10));
    }
}
