package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class EnderSlayer extends CustomEnchantment {

    private static final Set<EntityType> ENDER_TYPES = Set.of(
            EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ENDER_DRAGON, EntityType.SHULKER
    );

    public EnderSlayer() {
        super("ender_slayer", "Ender Slayer", 5, EnchantTier.ELITE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Increases damage dealt to Enderman and Ender dragons.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (ENDER_TYPES.contains(target.getType())) {
            event.setDamage(event.getDamage() * (1 + level * 0.25));
        }
    }
}
