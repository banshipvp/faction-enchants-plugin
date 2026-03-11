package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * Skill Swipe V — Sword enchantment, UNIQUE tier.
 * A chance to steal some of your enemy's EXP every time you damage them.
 */
public class SkillSwipe extends CustomEnchantment {

    private static final Random random = new Random();

    public SkillSwipe() {
        super("skillswipe", "Skill Swipe", 5, EnchantTier.UNIQUE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to steal some of your enemy's EXP every time you damage them.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // 15% chance per level (75% max at V)
        if (random.nextInt(100) >= level * 15) return;

        int stolenXp = level * 3;

        if (target instanceof Player targetPlayer) {
            int targetTotal = targetPlayer.getTotalExperience();
            int actualSteal = Math.min(stolenXp, Math.max(0, targetTotal));
            if (actualSteal > 0) {
                targetPlayer.giveExp(-actualSteal);
                attacker.giveExp(actualSteal);
                attacker.sendMessage("§6[Skill Swipe] §aStole §e" + actualSteal + " XP §afrom your target!");
                return;
            }
        }

        // Non-player target or player had no XP — give flat XP reward
        attacker.giveExp(stolenXp);
        attacker.sendMessage("§6[Skill Swipe] §aGained §e" + stolenXp + " XP!");
    }
}
