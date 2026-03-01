package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlanetaryDeathbringer extends CustomEnchantment {

    public PlanetaryDeathbringer() {
        super("planetary_deathbringer", "Planetary Deathbringer", 5, EnchantTier.HEROIC, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Massive damage based on enemy missing health.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double missing = target.getMaxHealth() - target.getHealth();
        event.setDamage(event.getDamage() + missing * level * 0.08);
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "deathbringer";
    }
}
