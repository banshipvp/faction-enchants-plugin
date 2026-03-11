package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Hellfire extends CustomEnchantment {

    public Hellfire() {
        super("hellfire", "Hellfire", 5, EnchantTier.ULTIMATE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "All arrows shot by you turn into explosive fireballs.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Explosion power scales with level (0.8 to 2.0), no fire, no block damage
        float power = 0.5f + level * 0.3f;
        target.getWorld().createExplosion(target.getLocation(), power, false, false);
    }
}
