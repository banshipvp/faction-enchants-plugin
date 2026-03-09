package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class Halloweenify extends CustomEnchantment {

    private final Random random = new Random();

    public Halloweenify() {
        super("halloweenify", "Halloweenify", 5, EnchantTier.MASTERY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Transforms attackers into random mobs temporarily.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 8 && attacker instanceof Player p) {
            // Placeholder for mob transformation (requires complex NMS)
            p.sendMessage("\u00a7cHalloweenify transforms you!");
        }
    }
}
