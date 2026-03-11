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
 * Rocket Escape III — Boots enchantment, ELITE tier.
 * Blasts the player into the air when their HP is low.
 */
public class RocketEscape extends CustomEnchantment {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 8000L;

    public RocketEscape() {
        super("rocketescape", "Rocket Escape", 3, EnchantTier.ELITE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Blast off into the air at low HP.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        double maxHealth = defender.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double healthAfter = defender.getHealth() - event.getFinalDamage();

        // Trigger when below 30% HP
        if (healthAfter > maxHealth * 0.30) return;

        long now = System.currentTimeMillis();
        Long lastUse = cooldowns.get(defender.getUniqueId());
        if (lastUse != null && now - lastUse < COOLDOWN_MS) return;
        cooldowns.put(defender.getUniqueId(), now);

        // Launch upward with increasing power per level
        double power = 0.8 + level * 0.4;
        defender.setVelocity(defender.getVelocity().setY(power));
    }
}
