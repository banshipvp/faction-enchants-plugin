package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class KillAura extends CustomEnchantment {

    private final Random random = new Random();

    public KillAura() {
        super("kill_aura", "Kill Aura", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Chance to strike nearby enemies with wither and weakness on kill.";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        for (Entity nearby : killer.getWorld().getNearbyEntities(killer.getLocation(), 5, 5, 5)) {
            if (nearby instanceof LivingEntity le && !nearby.equals(killer)) {
                le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, level * 40, level - 1));
                le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, level * 60, level - 1));
            }
        }
    }
}
