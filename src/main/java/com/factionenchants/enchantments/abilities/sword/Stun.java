package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Stun extends CustomEnchantment {

    private final Random random = new Random();

    public Stun() {
        super("stun", "Stun", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Stuns the opponent briefly on hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 8) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 10, true, false));
            target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, -5, true, false));
        }
    }
}
