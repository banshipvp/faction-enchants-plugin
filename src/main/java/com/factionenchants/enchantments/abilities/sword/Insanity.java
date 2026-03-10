package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Insanity VIII — Axe enchantment (LEGENDARY).
 * "You swing your axe like a maniac." Multiplies damage against players who are
 * wielding a Sword in their main hand when they are hit.
 * Multiplier: 1.0 + level * 0.10 (up to 1.8× at VIII).
 */
public class Insanity extends CustomEnchantment {

    public Insanity() {
        super("insanity", "Insanity", 8, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "You swing your axe like a maniac. Multiplies damage against players who are wielding a SWORD at the time they are hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;

        ItemStack mainHand = victim.getInventory().getItemInMainHand();
        if (!isSword(mainHand.getType())) return;

        double multiplier = 1.0 + level * 0.10;
        event.setDamage(event.getDamage() * multiplier);
    }

    private boolean isSword(Material mat) {
        return mat == Material.WOODEN_SWORD || mat == Material.STONE_SWORD
                || mat == Material.IRON_SWORD || mat == Material.GOLDEN_SWORD
                || mat == Material.DIAMOND_SWORD || mat == Material.NETHERITE_SWORD;
    }
}
