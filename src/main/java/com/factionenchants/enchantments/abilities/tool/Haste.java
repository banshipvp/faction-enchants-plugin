package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Haste extends CustomEnchantment {

    public Haste() {
        super("haste", "Haste", 3, EnchantTier.SIMPLE, ApplicableGear.TOOL_ALL);
    }

    @Override
    public String getDescription() {
        return "Allows you to swing your tools faster.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60, level - 1, true, false));
    }
}
