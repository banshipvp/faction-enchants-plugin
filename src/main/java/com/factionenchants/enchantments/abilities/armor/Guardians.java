package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Guardians extends CustomEnchantment {

    private final Random random = new Random();
    /** Maps golem UUID -> owner (player) UUID. Used by BloodLink. */
    public static final Map<UUID, UUID> GOLEM_OWNERS = new ConcurrentHashMap<>();

    public Guardians() {
        super("guardians", "Guardians", 10, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "A chance to spawn iron golems to assist you and watch over you.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 4) {
            double ox = (random.nextDouble() - 0.5) * 4;
            double oz = (random.nextDouble() - 0.5) * 4;
            IronGolem golem = defender.getWorld().spawn(defender.getLocation().add(ox, 0, oz), IronGolem.class);
            golem.setPlayerCreated(true);
            GOLEM_OWNERS.put(golem.getUniqueId(), defender.getUniqueId());
        }
    }
}
