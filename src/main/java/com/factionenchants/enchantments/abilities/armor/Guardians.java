package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Guardians extends CustomEnchantment {

    /** Maps golem UUID → owner player UUID so BloodLink can react to golem damage. */
    public static final Map<UUID, UUID> GOLEM_OWNERS = new HashMap<>();

    private final Random random = new Random();

    public Guardians() {
        super("guardians", "Guardians", 10, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "3% chance per level to spawn iron golems (30 second duration) to assist you.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Suppressed by Exterminator
        if (Exterminator.isSuppressed(defender.getUniqueId())) return;
        // Chance: level * 3% to spawn a golem
        if (random.nextInt(100) >= level * 3) return;

        Location loc = defender.getLocation().add(
                (random.nextDouble() * 4 - 2),
                0,
                (random.nextDouble() * 4 - 2)
        );
        IronGolem golem = (IronGolem) defender.getWorld().spawnEntity(loc, EntityType.IRON_GOLEM);
        golem.setPlayerCreated(true);
        GOLEM_OWNERS.put(golem.getUniqueId(), defender.getUniqueId());
        // Target the attacker if possible
        if (attacker instanceof org.bukkit.entity.LivingEntity livingAttacker) {
            golem.setTarget(livingAttacker);
        }
        // Auto-remove after 30 seconds
        org.bukkit.plugin.Plugin plugin = org.bukkit.Bukkit.getPluginManager().getPlugins().length > 0
                ? org.bukkit.Bukkit.getPluginManager().getPlugin("FactionEnchants") : null;
        if (plugin != null) {
            org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, golem::remove, 600L);
        }
    }
}
