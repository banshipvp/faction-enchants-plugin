package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Marksman extends CustomEnchantment {

    public Marksman() {
        super("marksman", "Marksman", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Increases bow damage by 8% per level (stackable across armor pieces).";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // +8% damage per level (stackable across armor pieces)
        event.setDamage(event.getDamage() * (1.0 + level * 0.08));
    }
}
