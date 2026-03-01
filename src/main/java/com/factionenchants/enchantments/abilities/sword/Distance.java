package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Distance extends CustomEnchantment {

    private final Random random = new Random();

    public Distance() {
        super("distance", "Distance", 4, EnchantTier.ULTIMATE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Teleport behind the opponent a random distance and gain regen.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 8) {
            Location behind = target.getLocation().add(
                    target.getLocation().getDirection().normalize().multiply(-(2 + level)));
            attacker.teleport(behind);
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, level - 1, true, false));
        }
    }
}
