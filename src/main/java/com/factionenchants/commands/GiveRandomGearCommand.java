package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveRandomGearCommand implements CommandExecutor {

    private final FactionEnchantsPlugin plugin;

    public GiveRandomGearCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("factionenchants.randomgear")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§eUsage: /<command> <player> <material> [minEnchants] [maxEnchants]");
            return true;
        }

        Player target = plugin.getServer().getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + args[0]);
            return true;
        }

        Material material;
        try {
            material = Material.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException ex) {
            sender.sendMessage("§cInvalid material: " + args[1]);
            return true;
        }

        if (!material.isItem()) {
            sender.sendMessage("§cThat material is not an item.");
            return true;
        }

        int min = Math.max(1, plugin.getConfig().getInt("random-gear.min-enchants", 1));
        int max = Math.max(min, plugin.getConfig().getInt("random-gear.max-enchants", 4));

        if (args.length >= 3) {
            try {
                min = Math.max(1, Integer.parseInt(args[2]));
            } catch (NumberFormatException ignored) {
                sender.sendMessage("§cInvalid minEnchants value.");
                return true;
            }
        }
        if (args.length >= 4) {
            try {
                max = Math.max(min, Integer.parseInt(args[3]));
            } catch (NumberFormatException ignored) {
                sender.sendMessage("§cInvalid maxEnchants value.");
                return true;
            }
        }

        ItemStack base = new ItemStack(material);
        ItemStack generated = plugin.getRandomGearManager().generateRandomEnchantedGear(base, min, max);
        target.getInventory().addItem(generated);

        sender.sendMessage("§aGave random enchanted gear to §e" + target.getName() + "§a.");
        target.sendMessage("§aYou received random enchanted gear!");
        return true;
    }
}
