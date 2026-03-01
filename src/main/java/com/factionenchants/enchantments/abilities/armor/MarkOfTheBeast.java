package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class MarkOfTheBeast extends CustomEnchantment {

    public MarkOfTheBeast() {
        super("mark_of_the_beast", "Mark of the Beast", 5, EnchantTier.MASTERY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Applies permanent wither to attackers.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (attacker instanceof org.bukkit.entity.LivingEntity le) {
            le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.WITHER, 200, level - 1, true, false));
        }
    }
}
