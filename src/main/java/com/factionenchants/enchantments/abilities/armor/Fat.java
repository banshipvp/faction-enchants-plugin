package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Fat extends CustomEnchantment {

    public Fat() {
        super("fat", "Fat", 5, EnchantTier.LEGENDARY, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Reduces knockback taken but slows you down slightly.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0, true, false));
    }

    @Override
    public void onHurtBy(Player defender, org.bukkit.entity.Entity attacker, int level, EntityDamageByEntityEvent event) {
        defender.setVelocity(defender.getVelocity().multiply(Math.max(0, 1 - level * 0.15)));
    }
}
