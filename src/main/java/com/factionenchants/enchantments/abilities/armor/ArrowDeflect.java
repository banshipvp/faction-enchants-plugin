package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ArrowDeflect extends CustomEnchantment {

    private final Random random = new Random();
    private static final Map<UUID, Long> lastDeflect = new HashMap<>();

    public ArrowDeflect() {
        super("arrow_deflect", "Arrow Deflect", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Prevents you from being damaged by enemy arrows more often than once every level x 400 milliseconds.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (event.getCause() != org.bukkit.event.entity.EntityDamageEvent.DamageCause.PROJECTILE) return;
        long now = System.currentTimeMillis();
        long cooldown = level * 400L;
        Long last = lastDeflect.get(defender.getUniqueId());
        if (last == null || now - last > cooldown) {
            if (random.nextInt(100) < level * 10) {
                event.setDamage(0);
                lastDeflect.put(defender.getUniqueId(), now);
            }
        }
    }
}
