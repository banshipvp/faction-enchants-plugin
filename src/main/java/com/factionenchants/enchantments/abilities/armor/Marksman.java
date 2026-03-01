package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Marksman extends CustomEnchantment {

    public Marksman() {
        super("marksman", "Marksman", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Increases your arrow damage.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        double bonus = 1 + level * 0.1;
        event.setDamage(event.getDamage() * bonus);
    }
}
