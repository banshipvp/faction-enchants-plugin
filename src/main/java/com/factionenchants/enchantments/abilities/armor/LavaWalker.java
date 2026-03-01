package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LavaWalker extends CustomEnchantment {

    public LavaWalker() {
        super("lava_walker", "Lava Walker", 3, EnchantTier.LEGENDARY, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Solidifies lava just beneath your feet and provides fire resistance.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0, true, false));
        Block below = player.getLocation().subtract(0, 1, 0).getBlock();
        if (below.getType() == Material.LAVA) {
            below.setType(Material.MAGMA_BLOCK);
        }
    }
}
