package com.factionenchants.enchantments.abilities.misc;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Impact extends CustomEnchantment {

    private final Random random = new Random();

    public Impact() {
        super("impact", "Impact", 4, EnchantTier.ELITE, ApplicableGear.TRIDENT);
    }

    @Override
    public String getDescription() {
        return "Chance to double dealt damage.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            event.setDamage(event.getDamage() * 2.0);
        }
    }
}
