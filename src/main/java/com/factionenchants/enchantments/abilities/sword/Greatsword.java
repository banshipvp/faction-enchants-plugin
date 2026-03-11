package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Greatsword V — Sword enchantment, ELITE tier.
 * Multiplies damage against players who are wielding a BOW at the time they are hit.
 */
public class Greatsword extends CustomEnchantment {

    public Greatsword() {
        super("greatsword", "Greatsword", 5, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Multiplies damage against players wielding a BOW at the time they are hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player targetPlayer)) return;

        ItemStack held = targetPlayer.getInventory().getItemInMainHand();
        String typeName = held.getType().name();
        if (!typeName.equals("BOW") && !typeName.equals("CROSSBOW")) return;

        // 15% bonus damage per level
        event.setDamage(event.getDamage() * (1.0 + (level * 0.15)));
    }
}
