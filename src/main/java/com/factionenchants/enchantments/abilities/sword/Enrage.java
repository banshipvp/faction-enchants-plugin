package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Enrage extends CustomEnchantment {

    public Enrage() {
        super("enrage", "Enrage", 3, EnchantTier.ULTIMATE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "The lower your HP is, the more damage you deal.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double maxHealth = attacker.getMaxHealth();
        double currentHealth = attacker.getHealth();
        // Missing HP fraction (0.0 = full health, 1.0 = 1 HP)
        double missingFraction = (maxHealth - currentHealth) / maxHealth;
        // Bonus damage: up to level * 40% at 0 HP, scales with missing HP
        double bonusMultiplier = 1.0 + (missingFraction * level * 0.4);
        event.setDamage(event.getDamage() * bonusMultiplier);
    }
}
