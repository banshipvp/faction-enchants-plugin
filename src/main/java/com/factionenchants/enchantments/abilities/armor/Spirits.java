package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class Spirits extends CustomEnchantment {

    private final Random random = new Random();

    public Spirits() {
        super("spirits", "Spirits", 10, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "A chance to spawn spirits when hit, which shoot fireballs at enemies.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 3) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("FactionEnchantsPlugin");
            if (plugin == null) return;
            Blaze blaze = defender.getWorld().spawn(defender.getLocation(), Blaze.class);
            Bukkit.getScheduler().runTaskLater(plugin, blaze::remove, 100L);
        }
    }
}
