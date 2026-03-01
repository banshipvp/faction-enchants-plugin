package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Enrage extends CustomEnchantment {

    private final Random random = new Random();

    public Enrage() {
        super("enrage", "Enrage", 3, EnchantTier.ULTIMATE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Deal more damage on low HP.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double hpRatio = attacker.getHealth() / attacker.getMaxHealth();
        if (hpRatio < 0.5) {
            double bonus = 1 + level * 0.15 * (1 - hpRatio);
            event.setDamage(event.getDamage() * bonus);
        }
    }
}
