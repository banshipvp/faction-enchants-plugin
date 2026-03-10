package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Heavy extends CustomEnchantment {

    public Heavy() {
        super("heavy", "Heavy", 5, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Decreases damage from enemy bows by 2% per level, this enchantment is stackable.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof Arrow)) return;
        double reduction = level * 0.02;
        event.setDamage(event.getDamage() * (1.0 - reduction));
    }
}
