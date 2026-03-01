package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Paralyze extends CustomEnchantment {

    private final Random random = new Random();

    public Paralyze() {
        super("paralyze", "Paralyze", 4, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Gives lightning effect and a chance for slowness and slow swinging.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 12) {
            target.getWorld().strikeLightningEffect(target.getLocation());
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40 + level * 20, level));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40 + level * 20, level - 1));
        }
    }
}
