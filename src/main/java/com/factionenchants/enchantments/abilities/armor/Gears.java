package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Gears III — Boots enchantment (LEGENDARY).
 * Grants the wearer a Speed buff while the boots are equipped.
 * Speed amplifier scales with level (level 1 = Speed I, etc.).
 */
public class Gears extends CustomEnchantment {

    public Gears() {
        super("gears", "Gears", 3, EnchantTier.LEGENDARY, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Added speed when equipped.";
    }

    @Override
    public void onTickPassive(Player player, int level, org.bukkit.inventory.ItemStack equipment) {
        // Apply Speed potion effect continuously (60-tick duration keeps it refreshed each tick)
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, level - 1, true, false, false));
    }
}
