package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Excavate extends CustomEnchantment {

    public Excavate() {
        super("excavate", "Excavate", 5, EnchantTier.UNIQUE, ApplicableGear.SHOVEL, ApplicableGear.PICKAXE);
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
        int range = level;
        Block target = player.getTargetBlock(null, 5);
        if (target == null || target.getType() == Material.AIR) return;
        // Determine face from player look direction
        org.bukkit.util.Vector dir = player.getLocation().getDirection();
        double ax = Math.abs(dir.getX()), ay = Math.abs(dir.getY()), az = Math.abs(dir.getZ());
        BlockFace face;
        if (ay > ax && ay > az) face = dir.getY() > 0 ? BlockFace.UP : BlockFace.DOWN;
        else if (ax > az) face = dir.getX() > 0 ? BlockFace.EAST : BlockFace.WEST;
        else face = dir.getZ() > 0 ? BlockFace.SOUTH : BlockFace.NORTH;
        List<Block> blocks = getBlocksInRadius(target, face, range);
        for (Block block : blocks) {
            if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
                block.breakNaturally(item);
            }
        }
    }

    private List<Block> getBlocksInRadius(Block target, BlockFace face, int range) {
        List<Block> blocks = new ArrayList<>();
        if (face == BlockFace.UP || face == BlockFace.DOWN) {
            for (int x = -range; x <= range; x++)
                for (int z = -range; z <= range; z++)
                    blocks.add(target.getRelative(x, 0, z));
        } else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            for (int x = -range; x <= range; x++)
                for (int y = -range; y <= range; y++)
                    blocks.add(target.getRelative(x, y, 0));
        } else {
            for (int z = -range; z <= range; z++)
                for (int y = -range; y <= range; y++)
                    blocks.add(target.getRelative(0, y, z));
        }
        return blocks;
    }
}
