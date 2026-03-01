package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ArrowLifesteal extends CustomEnchantment {

    private final Random random = new Random();

    public ArrowLifesteal() {
        super("arrow_lifesteal", "Arrow Lifesteal", 5, EnchantTier.ULTIMATE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Chance to steal health from opponent.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            double stolen = 0.5 + level * 0.5;
            shooter.setHealth(Math.min(shooter.getHealth() + stolen, shooter.getMaxHealth()));
        }
    }
}
