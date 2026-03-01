package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Reforged extends CustomEnchantment {

    private final Random random = new Random();

    public Reforged() {
        super("reforged", "Reforged", 10, EnchantTier.ELITE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Protects weapons durability, items will take longer to break.";
    }

    // Handled via PlayerItemDamageEvent - see EnchantListener
    public boolean shouldCancelDamage(int level) {
        return random.nextInt(100) < level * 8;
    }
}
