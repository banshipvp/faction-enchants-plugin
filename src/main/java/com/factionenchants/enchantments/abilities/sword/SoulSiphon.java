package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class SoulSiphon extends CustomEnchantment {

    public SoulSiphon() {
        super("soul_siphon", "Soul Siphon", 5, EnchantTier.MASTERY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Drains enemy health and gives it to you.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double drain = Math.min(target.getHealth() * 0.1, level * 2.0);
        target.damage(drain, attacker);
        attacker.setHealth(Math.min(attacker.getHealth() + drain, attacker.getMaxHealth()));
    }
}
