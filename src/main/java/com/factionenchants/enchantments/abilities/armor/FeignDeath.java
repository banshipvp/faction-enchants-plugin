package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FeignDeath extends CustomEnchantment {

    public FeignDeath() {
        super("feign_death", "Feign Death", 5, EnchantTier.MASTERY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Fakes death to escape combat.";
    }

    @Override
    public void onHurtBy(Player defender, org.bukkit.entity.Entity attacker, int level, org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (defender.getHealth() - event.getFinalDamage() < defender.getMaxHealth() * 0.1) {
            defender.setHealth(0.5);
            defender.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, level * 100, 0, true, false));
            defender.sendMessage("\u00a76Feign Death saves you!");
        }
    }
}
