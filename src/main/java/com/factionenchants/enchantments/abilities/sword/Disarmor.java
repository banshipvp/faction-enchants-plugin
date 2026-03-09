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
        super("disarmor", "Disarmor", 8, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A slight chance of removing one piece of armor from your enemy when they are at low health.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player p)) return;
        // Only triggers when target is at or below 50% max health
        if (p.getHealth() > p.getMaxHealth() * 0.5) return;
        // Slight base chance + small per-level bonus
        if (random.nextInt(100) >= 5 + level * 2) return;

        ItemStack[] armor = p.getInventory().getArmorContents();
        for (int i = armor.length - 1; i >= 0; i--) {
            if (armor[i] != null && armor[i].getType() != org.bukkit.Material.AIR) {
                p.getWorld().dropItemNaturally(p.getLocation(), armor[i]);
                armor[i] = null;
                p.getInventory().setArmorContents(armor);
                p.sendMessage("\u00a7cDisarmor stripped a piece of your armor!");
                break;
            }
        }
    }
}
