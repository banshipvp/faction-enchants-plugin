package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Rogue extends CustomEnchantment {

    public Rogue() {
        super("rogue", "Rogue", 5, EnchantTier.SOUL, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Passively grants speed and reduces fall damage.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, level - 1, true, false));
    }

    @Override
    public void onHurtBy(Player defender, org.bukkit.entity.Entity attacker, int level, org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL) {
            event.setDamage(event.getDamage() * (1 - level * 0.15));
        }
    }
}
