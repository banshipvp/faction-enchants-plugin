package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Experience extends CustomEnchantment {

    private final Random random = new Random();

    public Experience() {
        super("experience", "Experience", 5, EnchantTier.SIMPLE, ApplicableGear.TOOL_ALL);
    }

    @Override
    public String getDescription() {
        return "Chance to get experience from mining.";
    }

    /** Called from EnchantListener on block break. */
    public void tryGiveExp(Player player, int level) {
        if (random.nextInt(100) < level * 8) {
            player.giveExp(level * 2 + random.nextInt(level * 3 + 1));
        }
    }
}
