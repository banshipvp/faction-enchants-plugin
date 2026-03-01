package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Drunk extends CustomEnchantment {

    public Drunk() {
        super("drunk", "Drunk", 4, EnchantTier.LEGENDARY, ApplicableGear.HELMET);
    }

    @Override
    public String getDescription() {
        return "Grants strength at the cost of causing nausea.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, level - 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0, true, false));
    }
}
