package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Block extends CustomEnchantment {

    private final Random random = new Random();

    public Block() {
        super("block", "Block", 3, EnchantTier.ULTIMATE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to redirect an attack.";
    }

    @Override
    public void onHurtBy(Player defender, org.bukkit.entity.Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10 && defender.isBlocking()) {
            double dmg = event.getDamage();
            event.setDamage(0);
            if (attacker instanceof LivingEntity le) {
                le.damage(dmg * 0.5, defender);
            }
        }
    }
}
