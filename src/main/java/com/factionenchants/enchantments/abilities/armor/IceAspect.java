package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class IceAspect extends CustomEnchantment {

    private final Random random = new Random();

    public IceAspect() {
        super("ice_aspect", "Ice Aspect", 3, EnchantTier.ULTIMATE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A chance of causing the slowness effect on your enemy.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 18) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 + level * 20, level - 1));
        }
    }
}
