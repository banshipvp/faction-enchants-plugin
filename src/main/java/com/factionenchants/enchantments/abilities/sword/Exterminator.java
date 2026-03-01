package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Exterminator extends CustomEnchantment {

    public Exterminator() {
        super("exterminator", "Exterminator", 5, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Deals bonus damage to undead mobs.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        String type = target.getType().name();
        if (type.contains("ZOMBIE") || type.contains("SKELETON") || type.contains("PHANTOM")
                || type.contains("DROWNED") || type.contains("WITHER") || type.contains("HUSK") || type.contains("STRAY")) {
            event.setDamage(event.getDamage() * (1 + level * 0.15));
        }
    }
}
