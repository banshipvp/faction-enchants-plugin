package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Disarmor extends CustomEnchantment {

    private final Random random = new Random();

    public Disarmor() {
        super("disarmor", "Disarmor", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Chance to knock off a piece of enemy armor.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (target instanceof Player p && random.nextInt(100) < level * 6) {
            ItemStack[] armor = p.getInventory().getArmorContents();
            for (int i = armor.length - 1; i >= 0; i--) {
                if (armor[i] != null && armor[i].getType() != org.bukkit.Material.AIR) {
                    p.getWorld().dropItemNaturally(p.getLocation(), armor[i]);
                    armor[i] = null;
                    p.getInventory().setArmorContents(armor);
                    p.sendMessage("\u00a7cDisarmor knocked off your armor!");
                    break;
                }
            }
        }
    }
}
