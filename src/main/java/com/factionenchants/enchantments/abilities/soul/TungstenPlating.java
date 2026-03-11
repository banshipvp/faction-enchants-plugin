package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TungstenPlating extends CustomEnchantment {

    public TungstenPlating() {
        super("tungsten_plating", "Tungsten Plating", 1, EnchantTier.SOUL, ApplicableGear.FISHING_ROD);
    }

    @Override
    public String getDescription() {
        return "Passive Soul Enchantment. Prevents your fishing rod from taking durability damage in exchange for souls. Costs 1-3 souls per use.";
    }

    @Override
    public int getSoulCostPerProc() { return 0; }

    @Override
    public int getSoulCostPerTick() { return 0; }

    /**
     * Returns the soul cost for preventing durability damage, or -1 if prevention should not occur.
     * Called from EnchantListener.onPlayerItemDamage.
     */
    public boolean tryPrevent(Player player, int level) {
        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return false;
        if (!plugin.getSoulManager().isSoulActive(player)) return false;
        // Cost: 1 + level (1–3 for L1)
        int cost = Math.min(level + 1, 3);
        return plugin.getSoulManager().consumeSouls(player, cost);
    }

    @Override
    public int onItemDamage(Player player, ItemStack item, int level, int originalDamage) {
        return tryPrevent(player, level) ? 0 : originalDamage;
    }
}
