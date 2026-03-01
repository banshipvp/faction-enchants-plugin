package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Snipe extends CustomEnchantment {

    public Snipe() {
        super("snipe", "Sniper", 5, EnchantTier.LEGENDARY, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Headshots with projectiles deal up to 3.5x damage.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double dist = shooter.getLocation().distance(target.getLocation());
        double bonus = Math.min(dist * 0.12 * level, level * 5.0);
        event.setDamage(event.getDamage() + bonus);
    }
}
