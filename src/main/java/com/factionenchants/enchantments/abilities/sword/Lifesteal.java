package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Lifesteal extends CustomEnchantment {

    public Lifesteal() {
        super("lifesteal", "Lifesteal", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Heals you for a portion of melee damage dealt.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double heal = event.getDamage() * level * 0.08;
        attacker.setHealth(Math.min(attacker.getHealth() + heal, attacker.getMaxHealth()));
    }
}
