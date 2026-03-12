package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Overload — Armor enchantment (LEGENDARY).
 * Temporarily increases the wearer's max health while the armor is equipped.
 * Overload I:   +2 hearts (+4 HP,  amplifier 0)
 * Overload II:  +4 hearts (+8 HP,  amplifier 1)
 * Overload III: +6 hearts (+12 HP, amplifier 2)
 *
 * Non-stackable: only the highest Overload level across all armor pieces applies.
 * Effects are automatically cleared when armor is removed.
 */
public class Overload extends CustomEnchantment {

    public Overload() {
        super("overload", "Overload", 3, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Increases max hearts.\nI: +2 hearts  II: +4 hearts  III: +6 hearts.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Level 1 → amplifier 0 (+2 hearts), Level 2 → amplifier 1 (+4 hearts), Level 3 → amplifier 2 (+6 hearts)
        int amplifier = level - 1;

        // Non-stackable: if a higher Overload is already active this tick, skip.
        // Minecraft natively keeps the higher amplifier when addPotionEffect is called
        // with a lower amp, but we skip early to avoid any race conditions.
        PotionEffect existing = player.getPotionEffect(PotionEffectType.HEALTH_BOOST);
        if (existing != null && existing.getAmplifier() > amplifier) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, amplifier, true, false, false));
    }

    @Override
    public void onArmorUnequip(Player player, ItemStack armor, int level) {
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
    }
}
