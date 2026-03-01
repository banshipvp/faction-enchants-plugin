package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VeinMiner extends CustomEnchantment {

    public VeinMiner() {
        super("vein_miner", "Vein Miner", 5, EnchantTier.ELITE, ApplicableGear.PICKAXE);
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
        Block target = player.getTargetBlock(null, 5);
        if (target == null || target.getType() == Material.AIR) return;
        int maxBlocks = level * 8;
        List<Block> vein = findVein(target, target.getType(), maxBlocks);
        for (Block block : vein) {
            block.breakNaturally(item);
        }
    }

    private List<Block> findVein(Block origin, Material type, int max) {
        List<Block> vein = new ArrayList<>();
        Queue<Block> queue = new LinkedList<>();
        Set<Block> visited = new HashSet<>();
        queue.add(origin);
        while (!queue.isEmpty() && vein.size() < max) {
            Block current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);
            if (current.getType() != type) continue;
            vein.add(current);
            for (int dx = -1; dx <= 1; dx++)
                for (int dy = -1; dy <= 1; dy++)
                    for (int dz = -1; dz <= 1; dz++)
                        queue.add(current.getRelative(dx, dy, dz));
        }
        return vein;
    }
}
