package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

/**
 * Debug command to clear health-related potion effects (Health Boost, Absorption).
 * Useful for testing Overload enchantment.
 */
public class ClearHealthCommand implements CommandExecutor {

    private final FactionEnchantsPlugin plugin;

    public ClearHealthCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        boolean clearedHealthBoost = player.hasPotionEffect(PotionEffectType.HEALTH_BOOST);
        boolean clearedAbsorption  = player.hasPotionEffect(PotionEffectType.ABSORPTION);

        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        player.removePotionEffect(PotionEffectType.ABSORPTION);

        // Clamp current HP to the (now lower) max health so it doesn't stay at the boosted amount
        double newMax = player.getMaxHealth();
        if (player.getHealth() > newMax) {
            player.setHealth(newMax);
        }

        if (clearedHealthBoost || clearedAbsorption) {
            player.sendMessage("§a✓ Cleared health effects:");
            if (clearedHealthBoost) player.sendMessage("§7  - Health Boost");
            if (clearedAbsorption)  player.sendMessage("§7  - Absorption");
        } else {
            player.sendMessage("§eNo health effects to clear.");
        }

        // Warn if the player still has Overload armor — the effect will reapply within ~1 second
        if (hasOverloadArmor(player)) {
            player.sendMessage("§eNote: You have Overload armor equipped — the health boost will reapply automatically.");
            player.sendMessage("§eRemove your Overload armor first if you want to test without it.");
        }

        return true;
    }

    private boolean hasOverloadArmor(Player player) {
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType().isAir()) continue;
            Map<CustomEnchantment, Integer> enchants =
                    plugin.getEnchantmentManager().getEnchantmentsOnItem(armor);
            for (CustomEnchantment ench : enchants.keySet()) {
                if (ench.getId().equalsIgnoreCase("overload")) return true;
            }
        }
        return false;
    }
}
