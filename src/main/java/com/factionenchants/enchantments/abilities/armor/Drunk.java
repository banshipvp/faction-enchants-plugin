package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.commands.BlessCommand;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Drunk IV — Helmet enchantment (LEGENDARY).
 * Continuously applies Slowness and Mining Fatigue to the wearer (representing
 * a "drunk" state). Each tick there is a (level * 15)% chance to also grant
 * Strength at an amplifier scaled by level.
 */
public class Drunk extends CustomEnchantment {

    public Drunk() {
        super("drunk", "Drunk", 4, EnchantTier.LEGENDARY, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Slowness and slow swinging with a chance to give buffed strength.";
    }

    @Override
    public void onTickPassive(Player player, int level, org.bukkit.inventory.ItemStack equipment) {
        // Skip debuffs while the player is blessed — BlessedEffectBlocker enforces this
        if (BlessCommand.BLESSED.contains(player.getUniqueId())) {
            // Still grant the Strength benefit even when blessed
            double strengthChance = level * 0.15;
            if (Math.random() < strengthChance) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, level - 1, true, true, true));
            }
            return;
        }

        // Drawbacks: Slowness I and Mining Fatigue I (slow_digging)
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0, true, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 0, true, false, false));

        // Chance to proc Strength
        double strengthChance = level * 0.15;
        if (Math.random() < strengthChance) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, level - 1, true, true, true));
        }
    }
}
