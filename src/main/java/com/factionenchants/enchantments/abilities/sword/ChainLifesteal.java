package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ChainLifesteal extends CustomEnchantment {

    public ChainLifesteal() {
        super("chain_lifesteal", "Chain Lifesteal", 5, EnchantTier.MASTERY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Lifesteal chains to nearby enemies.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double heal = event.getDamage() * level * 0.1;
        attacker.setHealth(Math.min(attacker.getHealth() + heal, attacker.getMaxHealth()));
        for (org.bukkit.entity.Entity nearby : attacker.getWorld().getNearbyEntities(target.getLocation(), 5, 5, 5)) {
            if (nearby instanceof LivingEntity le && !nearby.equals(attacker) && !nearby.equals(target)) {
                le.damage(heal * 0.5, attacker);
            }
        }
    }
}
