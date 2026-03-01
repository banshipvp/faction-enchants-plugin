package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class JellyLegs extends CustomEnchantment {

    private final Random random = new Random();

    public JellyLegs() {
        super("jelly_legs", "Jelly Legs", 3, EnchantTier.ULTIMATE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Chance to negate fall damage.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL) {
            if (random.nextInt(100) < 40 + level * 20) {
                event.setDamage(0);
            }
        }
    }
}
