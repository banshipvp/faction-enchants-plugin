package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Extended Looting — pushes mob loot beyond vanilla Looting III.
 *
 * <ul>
 *   <li><b>Level I → Looting IV</b>: each drop item gets +1 extra unit.</li>
 *   <li><b>Level II → Looting V</b>: each drop item gets +2 extra units.</li>
 * </ul>
 *
 * The actual drop mutation is handled by {@link com.factionenchants.listeners.ExtendedLootingListener}
 * via {@code EntityDeathEvent}.
 */
public class ExtendedLooting extends CustomEnchantment {

    public ExtendedLooting() {
        super("extended_looting",
              "Extended Looting",
              2,
              EnchantTier.ELITE,
              ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Extends looting beyond vanilla III — level I = Looting IV, level II = Looting V.";
    }

    /** Returns the roman numeral display name for a given enchant level (1→IV, 2→V). */
    public static String lootingLevelName(int level) {
        return switch (level) {
            case 1  -> "IV";
            case 2  -> "V";
            default -> String.valueOf(level + 3);
        };
    }

    // Drop bonus is +level items per drop type (handled in ExtendedLootingListener)
    public static int extraDropsPerItem(int enchantLevel) {
        return enchantLevel; // level 1 → +1, level 2 → +2
    }
}
