package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Trickster extends CustomEnchantment {

    private final Random random = new Random();

    public Trickster() {
        super("trickster", "Trickster", 8, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "When hit you have a chance to teleport directly behind your opponent and take them by surprise.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 5) {
            // Teleport behind the attacker
            org.bukkit.Location behind = attacker.getLocation().clone();
            behind.add(attacker.getLocation().getDirection().multiply(-1.5));
            behind.setYaw(attacker.getLocation().getYaw() + 180);
            defender.teleport(behind);
            defender.sendMessage("\u00a7dYou teleported behind your enemy!");
        }
    }
}
