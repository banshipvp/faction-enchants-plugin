package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Overload III — Armor enchantment (LEGENDARY).
 * Permanently increases the wearer's max health while the armor is equipped.
 * Bonus health per level: 2 hearts (4 HP). Uses an AttributeModifier so that
 * removing the armor restores the original max health.
 *
 * The modifier is refreshed on each passive tick if not already present.
 */
public class Overload extends CustomEnchantment {

    private static final UUID MODIFIER_UUID = UUID.fromString("b4c3d2e1-f0a9-4b8c-7d6e-5c4b3a2f1e0d");
    private static final String MODIFIER_NAME = "overload_max_health";

    public Overload() {
        super("overload", "Overload", 3, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Permanent increase in hearts.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attr == null) return;

        // Check if already applied
        boolean hasModifier = attr.getModifiers().stream()
                .anyMatch(m -> m.getUniqueId().equals(MODIFIER_UUID));
        if (hasModifier) return;

        // Add +4 HP (2 hearts) per level
        double bonus = level * 4.0;
        AttributeModifier modifier = new AttributeModifier(MODIFIER_UUID, MODIFIER_NAME,
                bonus, AttributeModifier.Operation.ADD_NUMBER);
        attr.addModifier(modifier);
        // Restore health to new max if needed
        if (player.getHealth() > attr.getValue()) {
            player.setHealth(attr.getValue());
        }
    }
}
