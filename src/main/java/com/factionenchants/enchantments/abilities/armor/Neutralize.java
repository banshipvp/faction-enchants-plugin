package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Neutralize extends CustomEnchantment {

    public Neutralize() {
        super("neutralize", "Neutralize", 5, EnchantTier.MASTERY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Removes all potion effects from attackers.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (attacker instanceof Player p) {
            for (org.bukkit.potion.PotionEffectType type : org.bukkit.potion.PotionEffectType.values()) {
                p.removePotionEffect(type);
            }
            p.sendMessage("\u00a7cNeutralize removes all your effects!");
        }
    }
}
