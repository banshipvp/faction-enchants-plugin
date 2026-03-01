package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TokenBoost extends CustomEnchantment {

    public TokenBoost() {
        super("token_boost", "Token Boost", 4, EnchantTier.LEGENDARY, ApplicableGear.PICKAXE, ApplicableGear.SHOVEL, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Increases the chance of token drops when mining.";
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
        player.sendMessage("§6Token Boost §7active! Multiplier: §e" + (1 + level * 0.25) + "x");
    }
}
