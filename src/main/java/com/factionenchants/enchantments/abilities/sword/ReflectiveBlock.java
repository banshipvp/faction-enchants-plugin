package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ReflectiveBlock extends CustomEnchantment {

    public ReflectiveBlock() {
        super("reflective_block", "Reflective Block", 3, EnchantTier.HEROIC, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Reflects damage when blocking.";
    }

    @Override
    public void onHurtBy(Player defender, org.bukkit.entity.Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (defender.isBlocking() && attacker instanceof LivingEntity le) {
            le.damage(event.getDamage() * level * 0.2, defender);
        }
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "block";
    }
}
