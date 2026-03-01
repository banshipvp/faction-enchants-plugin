package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Blessed extends CustomEnchantment {

    private final Random random = new Random();

    public Blessed() {
        super("blessed", "Blessed", 4, EnchantTier.ULTIMATE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "A chance of removing debuffs.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            attacker.removePotionEffect(PotionEffectType.SLOW);
            attacker.removePotionEffect(PotionEffectType.WEAKNESS);
            attacker.removePotionEffect(PotionEffectType.POISON);
            attacker.removePotionEffect(PotionEffectType.WITHER);
            attacker.removePotionEffect(PotionEffectType.BLINDNESS);
            attacker.removePotionEffect(PotionEffectType.CONFUSION);
            attacker.removePotionEffect(PotionEffectType.SLOW_DIGGING);
            attacker.sendMessage("\u00a7aBlessed removed your debuffs!");
        }
    }
}
