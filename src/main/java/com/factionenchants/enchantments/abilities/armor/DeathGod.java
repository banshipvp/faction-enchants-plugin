package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DeathGod extends CustomEnchantment {

    public DeathGod() {
        super("death_god", "Death God", 3, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "When your health drops below 20%, become nearly invulnerable briefly.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (defender.getHealth() - event.getFinalDamage() < defender.getMaxHealth() * 0.2) {
            defender.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, level * 60, 4, true, false));
            defender.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, level * 40, 2, true, false));
        }
    }
}
