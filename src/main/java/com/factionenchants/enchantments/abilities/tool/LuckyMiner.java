package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LuckyMiner extends CustomEnchantment {

    private final Random random = new Random();

    public LuckyMiner() {
        super("lucky_miner", "Lucky Miner", 5, EnchantTier.UNIQUE, ApplicableGear.PICKAXE);
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
        double chance = level * 5.0;
        if (random.nextDouble() * 100 < chance) {
            int xp = level * 10 + random.nextInt(10);
            player.giveExp(xp);
            player.sendMessage("§bLucky Miner §7triggered! +§a" + xp + " XP");
        }
    }
}
