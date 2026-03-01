package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ExplosiveBow extends CustomEnchantment {

    public ExplosiveBow() {
        super("explosive", "Explosive", 5, EnchantTier.UNIQUE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Explosive arrows.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        float power = 0.5f + level * 0.5f;
        target.getWorld().createExplosion(target.getLocation(), power, false, false);
    }
}
