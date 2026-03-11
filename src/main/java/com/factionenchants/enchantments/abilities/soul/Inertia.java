package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Inertia extends CustomEnchantment {

    public Inertia() {
        super("inertia", "Inertia", 4, EnchantTier.SOUL, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Passive Soul Enchantment. Prevents you from being affected by Slownesses in exchange for souls. Costs 5-25 souls per use. This enchantment does not block Freezes.";
    }

    @Override
    public int getSoulCostPerTick() { return 0; } // handled manually below

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        if (!player.hasPotionEffect(PotionEffectType.SLOW)) return;

        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchantsPlugin");
        if (plugin == null) return;
        if (!plugin.getSoulManager().isSoulActive(player)) return;

        // Cost scales: 5 per level (5, 10, 15, 20, 25 for L1-L5, capped at 25 for L4)
        int cost = Math.min(level * 5, 25);
        if (plugin.getSoulManager().consumeSouls(player, cost)) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
    }
}
