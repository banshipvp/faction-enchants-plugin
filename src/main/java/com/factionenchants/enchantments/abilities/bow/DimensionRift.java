package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class DimensionRift extends CustomEnchantment {

    private final Random random = new Random();

    public DimensionRift() {
        super("dimension_rift", "Dimension Rift", 4, EnchantTier.ULTIMATE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Chance to turn blocks underneath target to soul sand, and possibly webs on top.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        int chance = level * 15;
        if (random.nextInt(100) >= chance) return;

        Location loc = target.getLocation();
        Block below = loc.getBlock().getRelative(0, -1, 0);
        if (below.getType().isSolid()) {
            below.setType(Material.SOUL_SAND);
        }

        // Higher levels: chance to place cobweb at feet
        if (level >= 2 && random.nextInt(100) < level * 20) {
            Block feet = loc.getBlock();
            if (feet.getType() == Material.AIR) {
                feet.setType(Material.COBWEB);
            }
        }
    }
}
