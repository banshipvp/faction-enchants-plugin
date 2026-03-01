package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Pummel extends CustomEnchantment {

    private final Random random = new Random();

    public Pummel() {
        super("pummel", "Pummel", 3, EnchantTier.ELITE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Chance to slow nearby enemy players for a short period.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            int radius = 4 + level;
            for (Entity e : target.getWorld().getNearbyEntities(target.getLocation(), radius, radius, radius)) {
                if (e instanceof Player p && !p.equals(attacker)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40 + level * 10, level - 1));
                }
            }
        }
    }
}
