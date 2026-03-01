package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Arsonist extends CustomEnchantment {

    public Arsonist() {
        super("arsonist", "Arsonist", 3, EnchantTier.ULTIMATE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Deals more damage while on fire.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (attacker.getFireTicks() > 0) {
            event.setDamage(event.getDamage() * (1 + level * 0.12));
        }
    }
}
