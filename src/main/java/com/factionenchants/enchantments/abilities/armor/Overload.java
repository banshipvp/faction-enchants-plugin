package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Overload III — Armor enchantment (LEGENDARY).
 * Temporarily increases the wearer's max health while the armor is equipped.
 * Overload I: +2 hearts (4 HP)
 * Overload II: +4 hearts (8 HP)
 * Overload III: +3 hearts (6 HP)
 * 
 * Effects are automatically cleared when armor is removed.
 */
public class Overload extends CustomEnchantment {

    public Overload() {
        super("overload", "Overload", 3, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Temporary increase in hearts.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Apply temporary health boost effect
        // Overload 1: +2 hearts (amp 0 = +4 HP), Overload 2: +4 hearts (amp 1 = +8 HP), Overload 3: +3 hearts (amp 0.5 = +6 HP)
        int amplifier = switch (level) {
            case 1 -> 0;  // +2 hearts (4 HP)
            case 2 -> 1;  // +4 hearts (8 HP)
            case 3 -> 0;  // Will give +2 hearts, then add a second effect for +1 heart
            default -> 0;
        };

        // Apply the health boost effect (60 tick duration, refreshed every tick)
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, amplifier, true, false, false));

        // For Overload III, add an additional +1 heart (2 HP) by giving Absorption
        if (level == 3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 60, 0, true, false, false));
        }
    }

    @Override
    public void onArmorUnequip(Player player, ItemStack armor, int level) {
        // Clear health boost and absorption effects when armor is removed
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        player.removePotionEffect(PotionEffectType.ABSORPTION);
    }
}
