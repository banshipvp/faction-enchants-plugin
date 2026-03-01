package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Demonforged extends CustomEnchantment {

    private final Random random = new Random();

    public Demonforged() {
        super("demonforged", "Demonforged", 4, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Increases durability loss on your enemy's armor.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;
        if (random.nextInt(100) < level * 12) {
            for (ItemStack armor : victim.getInventory().getArmorContents()) {
                if (armor == null || !armor.hasItemMeta()) continue;
                ItemMeta meta = armor.getItemMeta();
                if (meta instanceof Damageable dmg) {
                    int newDmg = Math.min(armor.getType().getMaxDurability(), dmg.getDamage() + level * 3);
                    dmg.setDamage(newDmg);
                    armor.setItemMeta(meta);
                }
            }
        }
    }
}
