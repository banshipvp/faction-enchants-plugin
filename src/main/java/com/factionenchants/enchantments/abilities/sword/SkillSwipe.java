package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class SkillSwipe extends CustomEnchantment {

    private final Random random = new Random();

    public SkillSwipe() {
        super("skill_swipe", "Skill Swipe", 5, EnchantTier.UNIQUE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to steal some of your enemy's EXP every time you damage them.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;
        if (random.nextInt(100) < level * 8) {
            int stolen = level + random.nextInt(level * 2 + 1);
            if (victim.getTotalExperience() >= stolen) {
                victim.giveExp(-stolen);
                attacker.giveExp(stolen);
                attacker.sendMessage("\u00a7aStole " + stolen + " XP from " + victim.getName() + "!");
            }
        }
    }
}
