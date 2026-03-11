package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Demonforged IV — Sword enchantment, ELITE tier.
 * Increases durability loss on your enemy's armor.
 */
public class Denonforged extends CustomEnchantment {

    public Denonforged() {
        super("denonforged", "Demonforged", 4, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Increases durability loss on your enemy's armor.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player targetPlayer)) return;

        for (ItemStack armor : targetPlayer.getInventory().getArmorContents()) {
            if (armor == null || armor.getType().isAir()) continue;
            ItemMeta meta = armor.getItemMeta();
            if (!(meta instanceof Damageable damageable)) continue;

            int maxDurability = armor.getType().getMaxDurability();
            int extra = 2 * level;
            int newDamage = Math.min(maxDurability - 1, damageable.getDamage() + extra);
            damageable.setDamage(newDamage);
            armor.setItemMeta(damageable);
        }
    }
}
