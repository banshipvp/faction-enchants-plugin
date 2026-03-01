package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Etheral extends CustomEnchantment {

    public Etheral() {
        super("etheral", "Etheral", 3, EnchantTier.SIMPLE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Gain haste upon killing mobs.";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        killer.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100 + level * 40, level - 1));
    }
}
