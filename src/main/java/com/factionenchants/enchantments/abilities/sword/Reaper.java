package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Reaper extends CustomEnchantment {

    private final Random random = new Random();

    public Reaper() {
        super("reaper", "Reaper", 4, EnchantTier.ELITE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "A chance to give your opponent the Wither and Blindness effects while dealing damage.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 10) {
            int dur = 60 + level * 20;
            target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, dur, level - 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, dur, 0));
        }
    }
}
