package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Sniper — Bow enchantment.
 * Each level grants a 5% chance (base 20%) to land a "headshot" that deals 1.25x damage.
 * At level 5 the chance reaches 45%.
 */
public class Sniper extends CustomEnchantment {

    private final Random random = new Random();

    public Sniper() {
        super("sniper", "Sniper", 5, EnchantTier.LEGENDARY, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Headshots deal up to 1.25x damage. Chance to headshot scales with level "
             + "(level 1 = 25%, level 5 = 45%).";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Base 20% + 5% per level
        int chance = 20 + level * 5;
        if (random.nextInt(100) < chance) {
            event.setDamage(event.getDamage() * 1.25);
            if (target instanceof Player p) {
                p.sendMessage("§cYou were headshotted!");
            }
            shooter.sendMessage("§aHeadshot!");
        }
    }
}
