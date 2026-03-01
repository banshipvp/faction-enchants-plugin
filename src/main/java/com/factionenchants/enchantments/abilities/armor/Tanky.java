package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Tanky extends CustomEnchantment {

    public Tanky() {
        super("tanky", "Tanky", 5, EnchantTier.ELITE, ApplicableGear.CHESTPLATE, ApplicableGear.LEGGINGS);
    }

    @Override
    public String getDescription() {
        return "Reduces incoming damage when hit.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, level - 1, true, false));
    }
}
