package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Thundering extends CustomEnchantment {

    private final Random random = new Random();

    public Thundering() {
        super("thundering", "Thundering Blow", 3, EnchantTier.SIMPLE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Can cause smite effect on your enemy.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < (level * 10)) {
            target.getWorld().strikeLightningEffect(target.getLocation());
            target.damage(level * 2.5, attacker);
        }
    }
}
