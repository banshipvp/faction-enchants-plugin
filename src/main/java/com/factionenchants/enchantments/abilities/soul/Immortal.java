package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Immortal extends CustomEnchantment {

    public Immortal() {
        super("immortal", "Immortal", 4, EnchantTier.SOUL, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Passive Soul Enchant. Prevents your armor from taking durability damage in exchange for souls. Costs 1-5 souls per use.";
    }

    @Override
    public int getSoulCostPerProc() { return 0; }

    @Override
    public int getSoulCostPerTick() { return 0; }

    /**
     * Called from EnchantListener.onPlayerItemDamage.
     * Returns the soul cost to prevent this durability event, or -1 if soul conditions are not met.
     */
    public int getSoulCostForDurability(int level) {
        return level; // 1–4 souls based on enchant level
    }

    /**
     * Tries to consume souls and prevent durability damage.
     * Returns true if durability damage should be cancelled.
     */
    public boolean tryPrevent(Player player, int level) {
        FactionEnchantsPlugin plugin = (FactionEnchantsPlugin) Bukkit.getPluginManager().getPlugin("FactionEnchantsPlugin");
        if (plugin == null) return false;
        if (!plugin.getSoulManager().isSoulActive(player)) return false;
        int cost = getSoulCostForDurability(level);
        return plugin.getSoulManager().consumeSouls(player, cost);
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Durability prevention is handled in EnchantListener.onPlayerItemDamage via tryPrevent()
    }
}
