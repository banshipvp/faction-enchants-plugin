package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class LethalSniper extends CustomEnchantment {

    public LethalSniper() {
        super("lethal_sniper", "Lethal Sniper", 5, EnchantTier.HEROIC, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Extreme bonus damage at long range.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        double dist = shooter.getLocation().distance(target.getLocation());
        if (dist > 20) {
            event.setDamage(event.getDamage() + level * 2.0);
        }
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "sniper";
    }
}
