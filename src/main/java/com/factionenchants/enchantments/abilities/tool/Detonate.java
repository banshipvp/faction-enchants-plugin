package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Detonate extends CustomEnchantment {

    public Detonate() {
        super("detonate", "Detonate", 9, EnchantTier.ULTIMATE, ApplicableGear.PICKAXE);
    }

    @Override
    public String getDescription() {
        return "Mines an area of blocks around the targeted block.\nActivates on every block break.";
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
        Block target = player.getTargetBlockExact(5);
        if (target == null || target.getType() == Material.AIR) return;

        int radius = level;
        List<Block> toBreak = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block b = target.getRelative(x, y, z);
                    Material t = b.getType();
                    if (t == Material.AIR || t == Material.BEDROCK) continue;
                    toBreak.add(b);
                }
            }
        }
        for (Block b : toBreak) {
            Material t = b.getType();
            // Liquids can't be broken naturally — just remove them
            if (t == Material.WATER || t == Material.LAVA) {
                b.setType(Material.AIR);
            } else if (player.getGameMode() == GameMode.CREATIVE) {
                b.setType(Material.AIR);
            } else {
                b.breakNaturally(item);
            }
        }
    }
}
