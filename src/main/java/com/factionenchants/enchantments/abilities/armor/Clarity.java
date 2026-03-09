package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Clarity — Armor enchantment.
 * Makes the wearer immune to blindness effects up to the amplifier equal to the enchant level.
 * Level 1 = immune to Blindness I, level 2 = immune up to Blindness II, etc.
 */
public class Clarity extends CustomEnchantment {

    public Clarity() {
        super("clarity", "Clarity", 3, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Makes you immune to Blindness effects up to the level of this enchantment.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        PotionEffect blind = player.getPotionEffect(PotionEffectType.BLINDNESS);
        if (blind != null && blind.getAmplifier() < level) {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }
}
