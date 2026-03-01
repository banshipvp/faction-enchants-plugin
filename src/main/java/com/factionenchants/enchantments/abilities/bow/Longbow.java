package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Longbow extends CustomEnchantment {

    public Longbow() {
        super("longbow", "Longbow", 4, EnchantTier.ULTIMATE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Deal more damage to enemies who are also wielding a bow.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (target instanceof Player p) {
            String held = p.getInventory().getItemInMainHand().getType().name();
            if (held.equals("BOW") || held.equals("CROSSBOW")) {
                event.setDamage(event.getDamage() * (1 + level * 0.1));
            }
        }
    }
}
