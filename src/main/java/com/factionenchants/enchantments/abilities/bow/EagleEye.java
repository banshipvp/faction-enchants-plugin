package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EagleEye extends CustomEnchantment {

    public EagleEye() {
        super("eagle_eye", "Eagle Eye", 5, EnchantTier.ULTIMATE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Arrow hits damage opponent's armor durability.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (target instanceof Player p) {
            for (ItemStack armor : p.getInventory().getArmorContents()) {
                if (armor != null && armor.getType() != org.bukkit.Material.AIR) {
                    org.bukkit.inventory.meta.Damageable meta = (org.bukkit.inventory.meta.Damageable) armor.getItemMeta();
                    if (meta != null) {
                        int newDmg = meta.getDamage() + level * 5;
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
