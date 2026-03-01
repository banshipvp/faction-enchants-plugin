package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Voodoo extends CustomEnchantment {

    private final Random random = new Random();

    public Voodoo() {
        super("voodoo", "Voodoo", 6, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Gives a chance to deal weakness.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof LivingEntity le)) return;
        if (random.nextInt(100) < level * 8) {
            le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80 + level * 20, level - 1));
        }
    }
}
