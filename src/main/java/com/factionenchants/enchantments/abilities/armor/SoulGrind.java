package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class SoulGrind extends CustomEnchantment {

    public SoulGrind() {
        super("soul_grind", "Soul Grind", 5, EnchantTier.MASTERY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Removes all positive effects from attackers.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (attacker instanceof Player p) {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.INCREASE_DAMAGE);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.DAMAGE_RESISTANCE);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.REGENERATION);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.ABSORPTION);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.SPEED);
            p.sendMessage("\u00a7cSoul Grind strips your buffs!");
        }
    }
}
