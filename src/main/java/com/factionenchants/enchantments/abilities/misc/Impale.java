package com.factionenchants.enchantments.abilities.misc;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Impale extends CustomEnchantment {

    public Impale() {
        super("impale", "Impale", 5, EnchantTier.LEGENDARY, ApplicableGear.TRIDENT);
    }

    @Override
    public String getDescription() {
        return "Deals extra damage in water or rain.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        boolean inWater = target.isInWater() || target.getWorld().hasStorm();
        if (inWater) {
            event.setDamage(event.getDamage() + level * 1.5);
        }
    }
}
