package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Unfocus extends CustomEnchantment {

    public Unfocus() {
        super("unfocus", "Unfocus", 5, EnchantTier.ULTIMATE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Hit enemies to reduce their bow damage for some time.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        int dur = level * 40;
        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, dur, level - 1, true, false));
    }
}
