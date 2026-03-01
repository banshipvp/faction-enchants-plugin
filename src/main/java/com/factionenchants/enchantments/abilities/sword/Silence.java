package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Silence extends CustomEnchantment {

    public Silence() {
        super("silence", "Silence", 5, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Prevents enemies from using potions by applying mining fatigue.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Mining fatigue as 'silence' (prevents inventory use on some servers)
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, level * 80, level - 1));
    }
}
