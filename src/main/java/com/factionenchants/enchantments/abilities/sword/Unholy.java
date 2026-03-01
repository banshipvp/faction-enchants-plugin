package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Unholy extends CustomEnchantment {

    public Unholy() {
        super("unholy", "Unholy", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Applies wither and removes regeneration from hit targets.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, level * 60, level - 1));
        target.removePotionEffect(PotionEffectType.REGENERATION);
        target.removePotionEffect(PotionEffectType.ABSORPTION);
    }
}
