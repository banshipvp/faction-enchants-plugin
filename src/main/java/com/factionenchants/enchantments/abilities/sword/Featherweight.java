package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Featherweight extends CustomEnchantment {

    private final Random random = new Random();

    public Featherweight() {
        super("featherweight", "Featherweight", 3, EnchantTier.UNIQUE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance to give a burst of haste.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60 + level * 20, level + 1));
        }
    }
}
