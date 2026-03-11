package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Implants extends CustomEnchantment {

    private final Random random = new Random();

    public Implants() {
        super("implants", "Implants", 3, EnchantTier.ULTIMATE, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Passively heals +1 health and restores +1 hunger every few seconds.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Apply Regeneration I — heals 1 HP every ~2.5 sec while effect is active.
        // Duration 40 ticks, renewed each second by the tick loop.
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 0, false, false, true));

        // Restore +1 hunger every (4 - level) seconds
        // Level 1: ~33% chance per second, Level 2: ~50%, Level 3: every second
        int chancePercent = level == 3 ? 100 : level == 2 ? 50 : 33;
        if (random.nextInt(100) < chancePercent) {
            int newFood = Math.min(player.getFoodLevel() + 1, 20);
            player.setFoodLevel(newFood);
        }
    }
}
