package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Lightning extends CustomEnchantment {

    private final Random random = new Random();

    public Lightning() {
        super("lightning", "Lightning", 3, EnchantTier.SIMPLE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "A chance to strike lightning at the opponent.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 12) {
            target.getWorld().strikeLightning(target.getLocation());
        }
    }
}
