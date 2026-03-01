package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class NetherSlayer extends CustomEnchantment {

    private static final Set<EntityType> NETHER_TYPES = Set.of(
            EntityType.BLAZE, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE, EntityType.WITHER_SKELETON, EntityType.MAGMA_CUBE,
            EntityType.GHAST, EntityType.HOGLIN, EntityType.ZOGLIN
    );

    public NetherSlayer() {
        super("nether_slayer", "Nether Slayer", 5, EnchantTier.ELITE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Increases damage dealt to Blazes and Zombie Pigmen.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (NETHER_TYPES.contains(target.getType())) {
            event.setDamage(event.getDamage() * (1 + level * 0.25));
        }
    }
}
