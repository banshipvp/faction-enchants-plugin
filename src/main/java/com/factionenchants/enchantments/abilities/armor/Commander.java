package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import local.simplefactions.FactionManager;
import local.simplefactions.SimpleFactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Commander V — Armor enchantment, UNIQUE tier.
 * Nearby faction allies are given Haste while you wear this enchant.
 */
public class Commander extends CustomEnchantment {

    public Commander() {
        super("commander", "Commander", 5, EnchantTier.UNIQUE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Nearby faction allies gain Haste (8+level×2 block radius, amplifier = level-1).";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        Plugin sfPlugin = Bukkit.getPluginManager().getPlugin("SimpleFactions");
        if (!(sfPlugin instanceof SimpleFactionsPlugin sfp)) return;

        FactionManager fm = sfp.getFactionManager();
        FactionManager.Faction myFaction = fm.getFaction(player.getUniqueId());
        if (myFaction == null) return;

        double radius = 8.0 + level * 2;
        for (Player nearby : player.getWorld().getPlayers()) {
            if (nearby.equals(player)) continue;
            if (nearby.getLocation().distanceSquared(player.getLocation()) > radius * radius) continue;
            FactionManager.Faction theirFaction = fm.getFaction(nearby.getUniqueId());
            if (theirFaction == null) continue;
            if (!theirFaction.getName().equalsIgnoreCase(myFaction.getName()) && !myFaction.isAlly(theirFaction.getName())) continue;
            nearby.addPotionEffect(new PotionEffect(
                    PotionEffectType.FAST_DIGGING, 60, level - 1, false, false));
        }
    }
}
