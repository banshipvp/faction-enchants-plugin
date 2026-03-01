package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Quiver extends CustomEnchantment {

    public Quiver() {
        super("quiver", "Quiver", 5, EnchantTier.LEGENDARY, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Arrows deal increasingly more damage the further you are from your target.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double dist = shooter.getLocation().distance(target.getLocation());
        double bonus = Math.min(dist / 10.0, level) * 0.5;
        event.setDamage(event.getDamage() + bonus);
    }
}
