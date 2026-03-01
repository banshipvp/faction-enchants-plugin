package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Lifebloom extends CustomEnchantment {

    private final Random random = new Random();

    public Lifebloom() {
        super("lifebloom", "Lifebloom", 5, EnchantTier.UNIQUE, ApplicableGear.LEGGINGS);
    }

    @Override
    public String getDescription() {
        return "Completely heals allies and truces on your death in 10 block radius.";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        // Note: heals allies when wearer is killed - handled via PlayerDeathEvent in CombatListener
    }

    /** Called from CombatListener when the wearer dies. */
    public void onWearerDeath(Player wearer, int level) {
        double radius = 10 + level * 2;
        for (org.bukkit.entity.Entity e : wearer.getWorld().getNearbyEntities(wearer.getLocation(), radius, radius, radius)) {
            if (!(e instanceof Player ally)) continue;
            if (ally.equals(wearer)) continue;
            ally.setHealth(ally.getMaxHealth());
            ally.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, level - 1));
            ally.sendMessage("\u00a7aLifebloom has healed you!");
        }
    }
}
