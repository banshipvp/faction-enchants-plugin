package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Sabotage extends CustomEnchantment {

    private final Random random = new Random();

    public Sabotage() {
        super("sabotage", "Sabotage", 5, EnchantTier.SOUL, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Damages enemy held item durability on hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (target instanceof Player p && random.nextInt(100) < level * 10) {
            ItemStack held = p.getInventory().getItemInMainHand();
            if (held != null && held.getType() != org.bukkit.Material.AIR) {
                org.bukkit.inventory.meta.Damageable meta = (org.bukkit.inventory.meta.Damageable) held.getItemMeta();
                if (meta != null) {
                    meta.setDamage(meta.getDamage() + level * 15);
                    held.setItemMeta(meta);
                }
            }
        }
    }
}
