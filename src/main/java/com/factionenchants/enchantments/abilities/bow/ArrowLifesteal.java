package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Arrow Lifesteal — Bow enchantment, ULTIMATE tier, max level V.
 * On arrow hit, has a level-scaled chance to steal health from the target
 * and give it to the shooter.
 * Heal amount: (level * 0.5) hearts, chance: level * 12%.
 */
public class ArrowLifesteal extends CustomEnchantment {

    private static final Random random = new Random();

    public ArrowLifesteal() {
        super("arrow_lifesteal", "Arrow Lifesteal", 5, EnchantTier.ULTIMATE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "A chance to steal health from opponent while fighting with a bow.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Chance: 12% per level (12% – 60%)
        if (random.nextInt(100) >= level * 12) return;

        double healAmount = level * 0.5; // 0.5–2.5 hearts per level
        double targetHealth = target.getHealth();
        double maxHealth = shooter.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double currentHealth = shooter.getHealth();

        // Steal min(healAmount, remaining target health) so we don't oversteal
        double stolen = Math.min(healAmount, Math.max(0, targetHealth - event.getFinalDamage()));
        double newHealth = Math.min(maxHealth, currentHealth + stolen);
        shooter.setHealth(newHealth);
        shooter.sendMessage("§c❤ §fArrow Lifesteal: stole §c" + String.format("%.1f", stolen) + " §fhearts.");
    }
}
