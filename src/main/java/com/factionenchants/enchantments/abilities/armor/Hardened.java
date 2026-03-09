package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class Hardened extends CustomEnchantment {

    private final Random random = new Random();

    public Hardened() {
        super("hardened", "Hardened", 3, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Armor takes less durability damage.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Durability preservation is handled via PlayerItemDamageEvent in EnchantListener
    }

    public boolean shouldCancelDamage(int level) {
        return random.nextInt(100) < level * 15; // 15% chance per level to prevent durability damage
    }
}
