package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class EnderWalker extends CustomEnchantment {

    private final Random random = new Random();

    public EnderWalker() {
        super("ender_walker", "Ender Walker", 5, EnchantTier.ULTIMATE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Wither and Poison do not injure and have a chance to heal at high levels.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Remove wither and poison effects so they cannot deal damage
        if (player.hasPotionEffect(PotionEffectType.WITHER)) {
            player.removePotionEffect(PotionEffectType.WITHER);
        }
        if (player.hasPotionEffect(PotionEffectType.POISON)) {
            player.removePotionEffect(PotionEffectType.POISON);
        }

        // At level 3+, chance to heal 1 HP for every wither/poison tick suppressed
        if (level >= 3 && random.nextInt(100) < level * 8) {
            double newHealth = Math.min(player.getHealth() + 1.0, player.getMaxHealth());
            player.setHealth(newHealth);
        }
    }
}
