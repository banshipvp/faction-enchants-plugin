package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Disintegrate extends CustomEnchantment {

    public Disintegrate() {
        super("disintegrate", "Disintegrate", 4, EnchantTier.ULTIMATE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Rapidly damages all enemy armor durability on hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (target instanceof Player p) {
            for (ItemStack armor : p.getInventory().getArmorContents()) {
                if (armor != null && armor.getType() != org.bukkit.Material.AIR) {
                    org.bukkit.inventory.meta.Damageable meta = (org.bukkit.inventory.meta.Damageable) armor.getItemMeta();
                    if (meta != null) {
                        int newDmg = meta.getDamage() + level * 8;
                        if (newDmg >= armor.getType().getMaxDurability()) {
                            armor.setAmount(0);
                        } else {
                            meta.setDamage(newDmg);
                            armor.setItemMeta(meta);
                        }
                    }
                }
            }
        }
    }
}
