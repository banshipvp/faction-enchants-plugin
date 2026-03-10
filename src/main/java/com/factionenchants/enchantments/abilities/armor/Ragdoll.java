package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Ragdoll extends CustomEnchantment {

    public Ragdoll() {
        super("ragdoll", "Ragdoll", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Whenever you take damage you are pushed far back.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (attacker == null) return;
        // Launch defender away from attacker
        Vector direction = defender.getLocation().toVector()
                .subtract(attacker.getLocation().toVector())
                .normalize()
                .multiply(level * 0.8)
                .setY(0.4);
        defender.setVelocity(direction);
    }
}
