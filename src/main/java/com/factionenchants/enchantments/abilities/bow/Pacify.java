package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Pacify extends CustomEnchantment {

    private final Random random = new Random();

    public Pacify() {
        super("pacify", "Pacify", 4, EnchantTier.ULTIMATE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "A chance to pacify your target, preventing them from building rage stacks for 1-3 seconds depending on level.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player targetPlayer)) return;
        // Chance: level * 15%
        if (random.nextInt(100) >= level * 15) return;

        // Duration: 1s at level 1, up to 3s at level 3+
        int durationTicks = Math.min(level, 3) * 20;
        // Weakness represents being pacified — unable to deal full damage
        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, durationTicks, 0, false, true, true));
        targetPlayer.sendMessage("\u00a75You have been Pacified!");
    }
}
