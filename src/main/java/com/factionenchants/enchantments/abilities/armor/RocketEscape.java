package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class RocketEscape extends CustomEnchantment {

    private final Random random = new Random();

    public RocketEscape() {
        super("rocket_escape", "Rocket Escape", 3, EnchantTier.ELITE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Blast off into the air at low HP.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (defender.getHealth() < defender.getMaxHealth() * 0.3 && random.nextInt(100) < level * 8) {
            defender.setVelocity(new Vector(0, 1.5 + level * 0.5, 0));
            defender.sendMessage("\u00a79Rocket Escape activated!");
        }
    }
}
