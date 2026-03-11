package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Paradox extends CustomEnchantment {

    public Paradox() {
        super("paradox", "Paradox", 5, EnchantTier.SOUL, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Passive Soul Enchantment. Heals all nearby allies in a massive area around you for a portion of all damage dealt to you. Costs 5 souls per use.";
    }

    @Override
    public int getSoulCostPerProc() { return 0; }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;
        if (!plugin.getSoulManager().isSoulActive(defender)) return;
        if (!plugin.getSoulManager().consumeSouls(defender, 5)) return;

        double damage = event.getFinalDamage();
        // Heal portion: level * 5% of damage taken
        double healAmount = damage * (level * 0.05);

        double radius = 5.0 + level * 1.0; // 6–10 blocks
        Location centre = defender.getLocation();

        for (Entity e : defender.getWorld().getNearbyEntities(centre, radius, radius, radius)) {
            if (!(e instanceof Player ally)) continue;
            if (e.equals(defender)) continue;

            double maxHp = ally.getMaxHealth();
            double newHp = Math.min(ally.getHealth() + healAmount, maxHp);
            ally.setHealth(newHp);
        }
    }
}
