package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Inversion extends CustomEnchantment {

    public Inversion() {
        super("inversion", "Inversion", 4, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Reflects a portion of damage taken back to the attacker.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double reflected = event.getDamage() * level * 0.1;
        if (attacker instanceof LivingEntity le) {
            le.damage(reflected, defender);
        }
    }
}
