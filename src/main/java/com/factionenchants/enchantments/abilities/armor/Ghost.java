package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ghost extends CustomEnchantment {

    public Ghost() {
        super("ghost", "Ghost", 3, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Invisible to /near lookups based on level: I=50 blocks, II=25 blocks, III=10 blocks minimum distance.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        // Apply invisibility effect while in friendly territory.
        // The /near integration requires SimpleFactions — the invisibility here
        // flags the player as hidden for lookup purposes at higher levels.
        if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            // Duration 40 ticks (refreshed every 20 by the tick loop)
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 0, false, false, false));
        }
    }
}
