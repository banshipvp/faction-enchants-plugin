package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Longbow extends CustomEnchantment {

    public Longbow() {
        super("longbow", "Longbow", 4, EnchantTier.ULTIMATE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Greatly increases damage dealt to enemy players that have a bow in their hands.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player targetPlayer)) return;
        ItemStack mainHand = targetPlayer.getInventory().getItemInMainHand();
        ItemStack offHand = targetPlayer.getInventory().getItemInOffHand();
        boolean targetHasBow = mainHand.getType().name().equals("BOW")
                || offHand.getType().name().equals("BOW")
                || mainHand.getType().name().equals("CROSSBOW")
                || offHand.getType().name().equals("CROSSBOW");
        if (!targetHasBow) return;
        // Increase damage by 25% per level
        event.setDamage(event.getDamage() * (1.0 + level * 0.25));
    }
}
