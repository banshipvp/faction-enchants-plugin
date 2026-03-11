package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

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

        // Clear health boost and absorption effects
        boolean clearedHealthBoost = player.hasPotionEffect(PotionEffectType.HEALTH_BOOST);
        boolean clearedAbsorption = player.hasPotionEffect(PotionEffectType.ABSORPTION);
        
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        player.removePotionEffect(PotionEffectType.ABSORPTION);

        if (clearedHealthBoost || clearedAbsorption) {
            player.sendMessage("§a✓ Cleared health effects:");
            if (clearedHealthBoost) player.sendMessage("§7  - Health Boost");
            if (clearedAbsorption) player.sendMessage("§7  - Absorption");
        } else {
            player.sendMessage("§eNo health effects to clear.");
        }

        return true;
    }
}
