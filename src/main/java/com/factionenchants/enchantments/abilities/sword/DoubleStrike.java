package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class DoubleStrike extends CustomEnchantment {

    private final Random random = new Random();

    public DoubleStrike() {
        super("double_strike", "Double Strike", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Chance to strike twice in one hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 8) {
            final double dmg = event.getDamage() * 0.5;
            Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchantsPlugin");
            if (plugin != null) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (target.isValid() && !target.isDead()) target.damage(dmg, attacker);
                }, 1L);
            }
        }
    }
}
