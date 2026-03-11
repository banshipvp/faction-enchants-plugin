package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import local.simplefactions.FactionManager;
import local.simplefactions.SimpleFactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Lifebloom V — Leggings enchantment, UNIQUE tier.
 * When the wearer dies, completely heals all faction-mates and allied faction members.
 */
public class Lifebloom extends CustomEnchantment {

    public Lifebloom() {
        super("lifebloom", "Lifebloom", 5, EnchantTier.UNIQUE, ApplicableGear.LEGGINGS);
    }

    @Override
    public String getDescription() {
        return "Completely heals allies and truces on your death.";
    }

    @Override
    public void onPlayerDeath(Player player, int level, ItemStack armor, PlayerDeathEvent event) {
        FactionManager.Faction deadFaction = getPlayerFaction(player);
        if (deadFaction == null) return;

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.equals(player)) continue;
            if (isAllyOrTeammate(deadFaction, online)) {
                double maxHp = online.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                online.setHealth(maxHp);
                online.sendMessage("§a[Lifebloom] §eYour ally has fallen. You have been completely healed!");
            }
        }
    }

    private FactionManager.Faction getPlayerFaction(Player player) {
        try {
            if (!Bukkit.getPluginManager().isPluginEnabled("SimpleFactions")) return null;
            SimpleFactionsPlugin sfp = SimpleFactionsPlugin.getInstance();
            if (sfp == null) return null;
            return sfp.getFactionManager().getFactionByPlayer(player);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isAllyOrTeammate(FactionManager.Faction deadFaction, Player other) {
        try {
            SimpleFactionsPlugin sfp = SimpleFactionsPlugin.getInstance();
            if (sfp == null) return false;
            FactionManager.Faction otherFaction = sfp.getFactionManager().getFactionByPlayer(other);
            if (otherFaction == null) return false;
            // Same faction members
            if (deadFaction.getName().equalsIgnoreCase(otherFaction.getName())) return true;
            // Allied factions
            return deadFaction.isAlly(otherFaction.getName());
        } catch (Exception e) {
            return false;
        }
    }
}
