package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Deathbringer extends CustomEnchantment {

    public Deathbringer() {
        super("deathbringer", "Deathbringer", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Increases damage dealt based on missing enemy health.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double missing = target.getMaxHealth() - target.getHealth();
        double bonus = missing * level * 0.04;
        event.setDamage(event.getDamage() + bonus);
    }
}
