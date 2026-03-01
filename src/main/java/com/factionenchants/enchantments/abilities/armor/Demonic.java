package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Demonic extends CustomEnchantment {

    public Demonic() {
        super("demonic", "Demonic", 3, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Removes fire resistance from those who attack you.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (attacker instanceof LivingEntity le) {
            le.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            if (attacker instanceof Player p) {
                p.sendMessage("\u00a7cDemonic armor has stripped your fire resistance!");
            }
        }
    }
}
