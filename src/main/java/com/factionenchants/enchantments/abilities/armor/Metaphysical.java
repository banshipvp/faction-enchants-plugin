package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Metaphysical extends CustomEnchantment {

    private final Random random = new Random();

    public Metaphysical() {
        super("metaphysical", "Metaphysical", 4, EnchantTier.ULTIMATE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "A chance to resist the slowness given by enemy Trap, Snare, and Funnel enchantments. At max level, you will only be affected approx. 10% of the time.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (!player.hasPotionEffect(PotionEffectType.SLOW)) return;
        // Resistance chance: level 1=40%, level 2=60%, level 3=80%, level 4=90%
        int resistChance = level == 4 ? 90 : level * 20;
        if (random.nextInt(100) < resistChance) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
    }
}
