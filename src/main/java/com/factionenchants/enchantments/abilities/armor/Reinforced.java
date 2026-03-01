package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Reinforced extends CustomEnchantment {

    public Reinforced() {
        super("reinforced", "Reinforced", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Reduces damage taken while being attacked from behind.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double angle = defender.getLocation().getYaw() - (float)(Math.toDegrees(
                Math.atan2(attacker.getLocation().getX() - defender.getLocation().getX(),
                           defender.getLocation().getZ() - attacker.getLocation().getZ())));
        angle = ((angle % 360) + 360) % 360;
        if (angle < 90 || angle > 270) {
            // attacker is behind
            double reduction = level * 0.1;
            event.setDamage(event.getDamage() * (1 - reduction));
        }
    }
}
