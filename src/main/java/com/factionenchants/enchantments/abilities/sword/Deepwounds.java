package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Deep Wounds — CosmicPVP-style Unique enchant.
 *
 * Each melee hit deals {@code level} extra hearts of true damage that
 * completely bypasses the target's armour. Damage is applied one tick
 * after the hit so vanilla's invincibility-frame system does not absorb it.
 *
 * Level scaling: 1 heart (2 HP) per enchantment level.
 *   Level 1 → 2 HP (1 heart)
 *   Level 2 → 4 HP (2 hearts)
 *   Level 3 → 6 HP (3 hearts)
 *   Level 4 → 8 HP (4 hearts)
 */
public class Deepwounds extends CustomEnchantment {

    public Deepwounds() {
        super("deepwounds", "Deepwounds", 4, EnchantTier.UNIQUE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Each hit deals true damage that bypasses armour.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        JavaPlugin plugin = JavaPlugin.getPlugin(FactionEnchantsPlugin.class);
        if (plugin == null) return;

        double trueDamage = level * 2.0; // 1 heart per level

        // Schedule one tick later so the hit's invincibility frames don't block our damage.
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (target.isDead() || !target.isValid()) return;

            double newHp = target.getHealth() - trueDamage;

            // Visual feedback
            target.getWorld().spawnParticle(
                    Particle.DAMAGE_INDICATOR,
                    target.getLocation().add(0, 1, 0),
                    (int) Math.ceil(trueDamage),
                    0.3, 0.5, 0.3, 0.08
            );

            // setHealth(0) triggers proper death — do NOT clamp above 0 if lethal
            target.setHealth(Math.max(0.0, newHp));
        }, 1L);
    }
}
