package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ObsidianBreaker extends CustomEnchantment {

    public ObsidianBreaker() {
        super("obsidian_breaker", "Obsidian Breaker", 1, EnchantTier.LEGENDARY, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Left-click obsidian to instantly break it.";
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
    }
}
