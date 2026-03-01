package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ArrowBreak extends CustomEnchantment {

    private final Random random = new Random();

    public ArrowBreak() {
        super("arrow_break", "Arrow Break", 6, EnchantTier.ULTIMATE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "A chance to deflect arrows while holding an axe.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.PROJECTILE) {
            if (random.nextInt(100) < level * 8) {
                event.setDamage(0);
                defender.sendMessage("\u00a7aArrow Break deflected an arrow!");
            }
        }
    }
}
