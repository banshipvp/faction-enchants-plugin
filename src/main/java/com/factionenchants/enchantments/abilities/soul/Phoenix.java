package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Phoenix extends CustomEnchantment {

    // Cooldown of ~2 minutes (120 000 ms)
    private static final long COOLDOWN_MS = 120_000;
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public Phoenix() {
        super("phoenix", "Phoenix", 3, EnchantTier.SOUL, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Passive Soul Enchantment. An attack that would normally kill you will instead heal you to full HP. Costs 500+ souls per use. Can only be activated once every couple minutes.";
    }

    @Override
    public int getSoulCostPerProc() { return 0; }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double finalDamage = event.getFinalDamage();
        if (finalDamage < defender.getHealth()) return; // would not kill

        long now = System.currentTimeMillis();
        UUID uid = defender.getUniqueId();
        if (now - cooldowns.getOrDefault(uid, 0L) < COOLDOWN_MS) return;

        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchantsPlugin");
        if (plugin == null) return;
        if (!plugin.getSoulManager().isSoulActive(defender)) return;

        // Cost scales: 500 + (level - 1) * 100
        int cost = 500 + (level - 1) * 100;
        if (!plugin.getSoulManager().consumeSouls(defender, cost)) return;

        cooldowns.put(uid, now);
        event.setCancelled(true);

        // Heal back to full on the next tick (avoid issues with cancelled event)
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (defender.isOnline()) {
                defender.setHealth(defender.getMaxHealth());
                defender.setFireTicks(0);
                defender.getWorld().spawnParticle(Particle.FLAME, defender.getLocation().add(0, 1, 0), 60, 0.5, 1.0, 0.5, 0.1);
                defender.getWorld().playSound(defender.getLocation(), Sound.ENTITY_WITHER_HURT, 1.0f, 1.8f);
                defender.sendMessage("\u00a7c\u2736 Your Phoenix enchant saved your life!");
            }
        }, 1L);
    }
}
