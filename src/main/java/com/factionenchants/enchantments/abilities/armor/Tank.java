package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Tank extends CustomEnchantment {

    public Tank() {
        super("tank", "Tank", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Reduces damage taken from axes.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (attacker instanceof Player p) {
            ItemStack held = p.getInventory().getItemInMainHand();
            if (held != null) {
                String name = held.getType().name();
                if (name.endsWith("_AXE")) {
                    double reduction = level * 0.08;
                    event.setDamage(event.getDamage() * (1 - reduction));
                }
            }
        }
    }
}
