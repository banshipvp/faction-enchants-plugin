package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Vampiric extends CustomEnchantment {

    public Vampiric() {
        super("vampiric", "Vampire", 3, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to heal you up to 3hp when you strike an enemy.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (Math.random() < 0.12 * level) {
            double heal = 0.5 + level * 0.5;
            attacker.setHealth(Math.min(attacker.getHealth() + heal, attacker.getMaxHealth()));
        }
    }
}
