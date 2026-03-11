package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class Dodge extends CustomEnchantment {

    private final Random random = new Random();

    public Dodge() {
        super("dodge", "Dodge", 5, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "6% per level chance to dodge attacks (11% per level while sneaking).";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        int baseChance = level * 6;
        if (defender.isSneaking()) baseChance += level * 5;
        if (random.nextInt(100) < baseChance) {
            event.setDamage(0);
            defender.sendMessage("\u00a75Dodged!");
        }
    }
}
