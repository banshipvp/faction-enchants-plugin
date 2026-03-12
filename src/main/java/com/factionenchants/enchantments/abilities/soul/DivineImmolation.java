package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DivineImmolation extends CustomEnchantment {

    private static final Set<UUID> ACTIVE = new HashSet<>();

    public DivineImmolation() {
        super("divine_immolation", "Divine Immolation", 4, EnchantTier.SOUL, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Active Soul Enchantment. Your weapons are imbued with divine fire, turning all your physical attacks into Area of Effect spells and igniting divine fire upon all nearby enemies. Costs 5 souls per second.";
    }

    /**
     * Soul cost per hit – treated as the "5 souls per second" equivalent
     * since a player typically hits ~1 target per second in melee.
     */
    @Override
    public int getSoulCostPerProc() {
        return 5;
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!ACTIVE.add(attacker.getUniqueId())) return; // already executing for this attacker

        double radius = 2.5 + (level * 0.5); // 3–4.5 blocks
        Location epicentre = target.getLocation();
        try {
            FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchants");
            if (plugin == null) return;
            if (!plugin.getSoulManager().isSoulActive(attacker)) return;
            if (!plugin.getSoulManager().consumeSouls(attacker, getSoulCostPerProc())) return;

            attacker.sendMessage("§6§l✦ DIVINE IMMOLATION");

            List<Entity> nearby = target.getWorld().getNearbyEntities(epicentre, radius, radius, radius)
                    .stream()
                    .filter(e -> e instanceof LivingEntity && !e.equals(attacker) && !e.equals(target))
                    .toList();

            for (Entity e : nearby) {
                LivingEntity le = (LivingEntity) e;
                le.setFireTicks(60 + level * 40); // 3–5 seconds of fire
                le.damage(level * 1.5, attacker);
            }

            // Visual + sound
            target.getWorld().spawnParticle(Particle.FLAME, epicentre, 30 + level * 10, radius / 2.0, 1.0, radius / 2.0, 0.05);
            target.getWorld().playSound(epicentre, Sound.ENTITY_BLAZE_SHOOT, 0.8f, 0.7f);
        } finally {
            ACTIVE.remove(attacker.getUniqueId());
        }
    }
}
