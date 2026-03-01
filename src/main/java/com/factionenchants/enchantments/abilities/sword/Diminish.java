package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Diminish extends CustomEnchantment {

    public Diminish() {
        super("diminish", "Diminish", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Reduces enemy max health on hit temporarily.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (target instanceof Player p) {
            double newMax = Math.max(2, p.getMaxHealth() - level * 0.5);
            p.setMaxHealth(newMax);
            if (p.getHealth() > newMax) p.setHealth(newMax);
            p.sendMessage("\u00a7cDiminish reduces your max health!");
        }
    }
}
