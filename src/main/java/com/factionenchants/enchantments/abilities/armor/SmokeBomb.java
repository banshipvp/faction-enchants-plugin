package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Smoke Bomb VIII — Helmet enchantment, ELITE tier.
 * When near death, spawns a smoke bomb cloud near enemies to distract them.
 */
public class SmokeBomb extends CustomEnchantment {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 12000L;

    public SmokeBomb() {
        super("smoke_bomb", "Smoke Bomb", 8, EnchantTier.ELITE, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "When you are near death, you will spawn a smoke bomb to distract your enemies.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double maxHealth = defender.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double healthAfter = defender.getHealth() - event.getFinalDamage();

        // Trigger at 25% HP or below
        if (healthAfter > maxHealth * 0.25) return;

        long now = System.currentTimeMillis();
        Long lastUse = cooldowns.get(defender.getUniqueId());
        if (lastUse != null && now - lastUse < COOLDOWN_MS) return;
        cooldowns.put(defender.getUniqueId(), now);

        // Spawn a blinding smoke cloud at each nearby enemy player's location
        double radius = 4.0 + level * 0.5;
        for (Entity nearby : defender.getNearbyEntities(radius, radius, radius)) {
            if (!(nearby instanceof Player) || nearby.equals(defender)) continue;
            AreaEffectCloud cloud = (AreaEffectCloud) defender.getWorld()
                    .spawnEntity(nearby.getLocation(), EntityType.AREA_EFFECT_CLOUD);
            cloud.setDuration(60 + level * 10);
            cloud.setRadius(2.5f);
            cloud.setRadiusPerTick(-0.02f);
            cloud.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                    40 + level * 10, 0), true);
            cloud.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION,
                    40 + level * 10, 0), true);
            cloud.setSource(defender);
        }
    }
}
