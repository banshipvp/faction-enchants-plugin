package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Telepathy extends CustomEnchantment {

    public Telepathy() {
        super("telepathy", "Telepathy", 4, EnchantTier.UNIQUE,
                ApplicableGear.PICKAXE, ApplicableGear.SHOVEL, ApplicableGear.AXE, ApplicableGear.HOE);
    }

    @Override
    public String getDescription() {
        return "Sends all block drops directly into your inventory.";
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
    }
}
