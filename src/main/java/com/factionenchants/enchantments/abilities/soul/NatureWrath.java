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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NatureWrath extends CustomEnchantment {

    // Cooldown: 8 seconds (160 ticks)
    private static final long COOLDOWN_MS = 8_000;
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public NatureWrath() {
        super("nature_wrath", "Nature Wrath", 4, EnchantTier.SOUL, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Passive Soul Enchantment. Temporarily freeze all enemies in a massive area around you, pushing them back and dealing massive nature damage. Costs 75 souls per use.";
    }

    @Override
    public int getSoulCostPerProc() { return 0; }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        long now = System.currentTimeMillis();
        UUID uid = defender.getUniqueId();
        if (now - cooldowns.getOrDefault(uid, 0L) < COOLDOWN_MS) return;

        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;
        if (!plugin.getSoulManager().isSoulActive(defender)) return;
        if (!plugin.getSoulManager().consumeSouls(defender, 75)) return;

        cooldowns.put(uid, now);

        double radius = 4.0 + level * 1.5; // 5.5–10 blocks
        Location centre = defender.getLocation();

        for (Entity e : defender.getWorld().getNearbyEntities(centre, radius, radius, radius)) {
            if (!(e instanceof LivingEntity target)) continue;
            if (e.equals(defender)) continue;

            // Freeze (slowness IV for 3 seconds)
            ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3, false, true, true));
            // Nature damage
            target.damage(3.0 + level * 2.0, defender);
            // Push back
            Vector push = e.getLocation().toVector().subtract(centre.toVector()).normalize().multiply(1.2 + level * 0.3);
            push.setY(0.4);
            e.setVelocity(push);
        }

        // Visual effect
        defender.getWorld().spawnParticle(Particle.COMPOSTER, centre, 80, radius / 2, 1.5, radius / 2, 0.1);
        defender.getWorld().playSound(centre, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 0.6f);
    }
}
