package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Hellfire extends CustomEnchantment {

    private final Random random = new Random();

    public Hellfire() {
        super("hellfire", "Hellfire", 5, EnchantTier.ULTIMATE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Arrows explode into fire on impact.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        target.getWorld().createExplosion(target.getLocation(), level * 0.4f, true, false);
        target.setFireTicks(level * 40);
    }
}
