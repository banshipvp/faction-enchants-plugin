package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Soulbound extends CustomEnchantment {

    public Soulbound() {
        super("soulbound", "Soulbound", 5, EnchantTier.HEROIC, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Prevents item loss on death.";
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
        // Soulbound logic handled in EnchantListener on death
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "abiding";
    }
}
