package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class Volley extends CustomEnchantment {

    private final Random random = new Random();

    public Volley() {
        super("volley", "Volley", 4, EnchantTier.ELITE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Fires multiple arrows per shot.";
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
        int arrows = level + 1;
        for (int i = 0; i < arrows; i++) {
            double spread = 0.1 * i;
            Vector dir = player.getLocation().getDirection()
                    .add(new Vector(
                            random.nextGaussian() * spread,
                            random.nextGaussian() * spread,
                            random.nextGaussian() * spread));
            Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation(), dir, 2.0f, 1.0f);
            arrow.setShooter(player);
        }
    }
}
