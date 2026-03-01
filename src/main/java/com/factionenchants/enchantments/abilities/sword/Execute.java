package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Execute extends CustomEnchantment {

    private final Random random = new Random();

    public Execute() {
        super("execute", "Execute", 7, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Damage buff when your target is at low HP.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double hpPercent = target.getHealth() / target.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
        if (hpPercent < 0.5) {
            double bonus = 1 + (level * 0.15) * (1 - hpPercent);
            event.setDamage(event.getDamage() * bonus);
        }
    }
}
