package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Valor extends CustomEnchantment {

    public Valor() {
        super("valor", "Valor", 5, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Reduces damage taken while wielding a sword.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        ItemStack held = defender.getInventory().getItemInMainHand();
        if (held != null && held.getType().name().endsWith("_SWORD")) {
            double reduction = level * 0.06;
            event.setDamage(event.getDamage() * (1 - reduction));
        }
    }
}
