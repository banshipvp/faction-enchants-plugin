package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Metaphysical extends CustomEnchantment {

    private final Random random = new Random();

    public Metaphysical() {
        super("metaphysical", "Metaphysical", 4, EnchantTier.ULTIMATE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "A chance to be immune to slowness.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (player.hasPotionEffect(PotionEffectType.SLOW) && random.nextInt(100) < level * 15) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
    }
}
