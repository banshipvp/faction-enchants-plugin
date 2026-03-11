package com.factionenchants.enchantments.abilities.axe;

import com.factionenchants.commands.BlessCommand;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Blessed — Axe enchantment, ELITE tier, max level IV.
 * On hit, has a level-scaled chance to remove negative potion effects (debuffs)
 * from the attacker.
 * Chance: level * 15% (15%–60%).
 */
public class Blessed extends CustomEnchantment {

    private static final Random random = new Random();

    /** Potion effects considered debuffs that Blessed can cleanse. */
    private static final List<PotionEffectType> DEBUFFS = List.of(
            PotionEffectType.POISON,
            PotionEffectType.WITHER,
            PotionEffectType.BLINDNESS,
            PotionEffectType.SLOW,
            PotionEffectType.WEAKNESS,
            PotionEffectType.CONFUSION,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.UNLUCK,
            PotionEffectType.HUNGER,
            PotionEffectType.LEVITATION
    );

    public Blessed() {
        super("blessed", "Blessed", 4, EnchantTier.ULTIMATE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "A chance of removing debuffs when you hit an enemy with your axe.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Chance: 15% per level (15%–60%)
        if (random.nextInt(100) >= level * 15) return;

        List<PotionEffectType> removed = new ArrayList<>();
        for (PotionEffectType debuff : DEBUFFS) {
            if (attacker.hasPotionEffect(debuff)) {
                attacker.removePotionEffect(debuff);
                removed.add(debuff);
            }
        }

        if (!removed.isEmpty()) {
            BlessCommand.BLESSED.add(attacker.getUniqueId());
            attacker.sendMessage("§a✦ §fBlessed cleansed §a" + removed.size() + " §fdebuff(s) from you!");
        }
    }
}
