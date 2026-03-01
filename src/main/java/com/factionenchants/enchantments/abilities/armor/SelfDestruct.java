package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SelfDestruct extends CustomEnchantment {

    private final Random random = new Random();

    public SelfDestruct() {
        super("self_destruct", "Self Destruct", 3, EnchantTier.UNIQUE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "When close to death, TNT spawns around you to end you and remove dropped items.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (defender.getHealth() - event.getFinalDamage() < 2.0) {
            Location loc = defender.getLocation();
            for (int i = 0; i < level + 1; i++) {
                double ox = (random.nextDouble() - 0.5) * 3;
                double oz = (random.nextDouble() - 0.5) * 3;
                TNTPrimed tnt = loc.getWorld().spawn(loc.clone().add(ox, 0.5, oz), TNTPrimed.class);
                tnt.setFuseTicks(10);
            }
        }
    }
}
