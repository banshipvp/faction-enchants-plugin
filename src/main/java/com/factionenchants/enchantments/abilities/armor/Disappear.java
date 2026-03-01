package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Disappear extends CustomEnchantment {

    public Disappear() {
        super("disappear", "Disappear", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Grants you invisibility when your HP is very low.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double hpRatio = (defender.getHealth() - event.getFinalDamage()) / defender.getMaxHealth();
        if (hpRatio < 0.25) {
            int dur = level * 40;
            defender.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, dur, 0, true, false));
        }
    }
}
