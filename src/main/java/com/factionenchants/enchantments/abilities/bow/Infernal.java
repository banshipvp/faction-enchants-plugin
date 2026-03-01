package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Infernal extends CustomEnchantment {

    private final Random random = new Random();

    public Infernal() {
        super("infernal", "Infernal", 3, EnchantTier.ELITE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Explosive fire effect.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < 40 + level * 10) {
            target.getWorld().createExplosion(target.getLocation(), level * 0.5f, true, false);
            target.setFireTicks(60 + level * 40);
        }
    }
}
