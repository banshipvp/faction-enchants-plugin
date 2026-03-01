package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ReinforcedTank extends CustomEnchantment {

    public ReinforcedTank() {
        super("reinforced_tank", "Reinforced Tank", 4, EnchantTier.HEROIC, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Extreme damage reduction from axes.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (attacker instanceof Player p) {
            String held = p.getInventory().getItemInMainHand().getType().name();
            if (held.endsWith("_AXE")) {
                event.setDamage(event.getDamage() * (1 - level * 0.12));
            }
        }
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "tank";
    }
}
