package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class EagleEye extends CustomEnchantment {

    private final Random random = new Random();

    public EagleEye() {
        super("eagle_eye", "Eagle Eye", 5, EnchantTier.ULTIMATE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Chance to deal 1-4 durability damage to ALL armor pieces of enemy player.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player targetPlayer)) return;
        // Chance: level * 12%
        if (random.nextInt(100) >= level * 12) return;

        for (ItemStack armor : targetPlayer.getInventory().getArmorContents()) {
            if (armor == null || armor.getType().isAir()) continue;
            ItemMeta meta = armor.getItemMeta();
            if (!(meta instanceof Damageable damageable)) continue;
            int dmg = 1 + random.nextInt(4); // 1-4 durability damage
            damageable.setDamage(damageable.getDamage() + dmg);
            armor.setItemMeta(damageable);
        }
        targetPlayer.updateInventory();
    }
}
