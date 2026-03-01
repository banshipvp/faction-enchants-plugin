package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AntiGravity extends CustomEnchantment {

    public AntiGravity() {
        super("anti_gravity", "Anti Gravity", 3, EnchantTier.ELITE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Super jump.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, level + 2, true, false));
    }
}
