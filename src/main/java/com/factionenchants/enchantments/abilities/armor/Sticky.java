package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Sticky — Armor enchantment, ULTIMATE tier, max level VIII.
 * Decreases the chance of an enemy's Disarmor enchantment procing on you
 * by 12.5% per level. At max level (VIII = 100%), you can never be disarmed.
 *
 * Other enchantments (e.g. Disarmor) should call {@link #getProtectionChance}
 * to check how much protection the target has before attempting to disarm.
 */
public class Sticky extends CustomEnchantment {

    /** Protection per level: 12.5% per level → 100% at level 8. */
    public static final double PROTECTION_PER_LEVEL = 12.5;

    public Sticky() {
        super("sticky", "Sticky", 8, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Decreases the chance of an enemy's Disarmor enchantment procing on you " +
               "by 12.5% per level. At max level, you can never be disarmed.";
    }

    /**
     * Returns the total Sticky protection percentage (0–100) for a player
     * by summing levels across all worn armor pieces. Capped at 100.
     */
    public static double getProtectionChance(Player player) {
        double total = 0;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null) continue;
            String typeName = armor.getType().name();
            boolean isArmor = typeName.endsWith("_HELMET")
                    || typeName.endsWith("_CHESTPLATE")
                    || typeName.endsWith("_LEGGINGS")
                    || typeName.endsWith("_BOOTS");
            if (!isArmor) continue;
            if (!armor.hasItemMeta() || armor.getItemMeta() == null) continue;
            if (!armor.getItemMeta().hasLore()) continue;
            for (String line : armor.getItemMeta().getLore()) {
                String stripped = line.replaceAll("§[0-9a-fk-or]", "").trim();
                if (stripped.startsWith("Sticky ")) {
                    String levelPart = stripped.substring("Sticky ".length()).trim();
                    int level = fromRoman(levelPart);
                    if (level > 0) {
                        total += level * PROTECTION_PER_LEVEL;
                    }
                }
            }
        }
        return Math.min(100.0, total);
    }

    /** Returns true if the player is fully immune to disarming. */
    public static boolean isFullyProtected(Player player) {
        return getProtectionChance(player) >= 100.0;
    }

    private static int fromRoman(String s) {
        return switch (s) {
            case "I" -> 1; case "II" -> 2; case "III" -> 3; case "IV" -> 4;
            case "V" -> 5; case "VI" -> 6; case "VII" -> 7; case "VIII" -> 8;
            default -> {
                try { yield Integer.parseInt(s); } catch (NumberFormatException e) { yield 0; }
            }
        };
    }
}
