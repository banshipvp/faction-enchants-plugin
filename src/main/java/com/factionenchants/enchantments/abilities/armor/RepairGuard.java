package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Repair Guard III — Armor enchantment, ELITE tier.
 * Whenever you remove a low durability piece of armor to repair it,
 * you will get up to 10 absorption hearts (depending on level) while fixing it.
 * Triggered via EnchantListener's InventoryClickEvent when armor is unequipped at low durability.
 */
public class RepairGuard extends CustomEnchantment {

    public RepairGuard() {
        super("repairguard", "Repair Guard", 3, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Removing low durability armor to repair gives you absorption hearts.";
    }

    @Override
    public void onArmorUnequip(Player player, ItemStack armor, int level) {
        // Level I: 2 hearts (amplifier 0 = 4 HP), Level II: 6 hearts (amplifier 2 = 12 HP),
        // Level III: 10 hearts (amplifier 4 = 20 HP)
        int amplifier = (level - 1) * 2;
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.ABSORPTION, 30 * 20, amplifier, false, true));
    }
}
