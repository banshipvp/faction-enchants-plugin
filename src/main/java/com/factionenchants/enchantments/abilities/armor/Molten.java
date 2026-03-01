package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Molten extends CustomEnchantment {

    private final Random random = new Random();

    public Molten() {
        super("molten", "Molten", 4, EnchantTier.UNIQUE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Chance of setting your attacker ablaze.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof LivingEntity le)) return;
        if (random.nextInt(100) < level * 12) {
            le.setFireTicks(40 + level * 30);
        }
    }
}
