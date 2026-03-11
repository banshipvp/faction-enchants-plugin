package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Tank — Armor enchantment, ULTIMATE tier, max level IV.
 * Decreases damage from enemy axes by 1.85% per level.
 * This enchantment is stackable (each armor piece contributes).
 * Handled directly in {@link #onHurtBy} by checking the attacker's weapon.
 */
public class Tank extends CustomEnchantment {

    /** Damage reduction per level per armor piece (1.85%). Stacks across pieces. */
    public static final double REDUCTION_PER_LEVEL = 0.0185;

    public Tank() {
        super("tank", "Tank", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Decreases damage from enemy axes by 1.85% per level, this enchantment is stackable.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof Player attackerPlayer)) return;
        ItemStack weapon = attackerPlayer.getInventory().getItemInMainHand();
        if (weapon == null) return;
        String type = weapon.getType().name();
        if (!type.endsWith("_AXE")) return;

        double reduction = level * REDUCTION_PER_LEVEL;
        event.setDamage(event.getDamage() * (1.0 - reduction));
    }
}
