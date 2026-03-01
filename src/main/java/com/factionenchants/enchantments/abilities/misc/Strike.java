package com.factionenchants.enchantments.abilities.misc;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Strike extends CustomEnchantment {

    private final Random random = new Random();

    public Strike() {
        super("strike", "Strike", 3, EnchantTier.SIMPLE, ApplicableGear.TRIDENT);
    }

    @Override
    public String getDescription() {
        return "Chance to strike lightning at the opponent.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 12) {
            target.getWorld().strikeLightning(target.getLocation());
        }
    }
}
