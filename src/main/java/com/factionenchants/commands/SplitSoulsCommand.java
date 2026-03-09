package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.items.SoulGemItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * /splitsouls <amount>
 *
 * Splits charges from the held Soul Gem into a new gem.
 * e.g. holding a 5000-charge gem and typing /splitsouls 500 leaves
 * the held gem with 4500 charges and adds a new 500-charge gem to inventory.
 */
public class SplitSoulsCommand implements CommandExecutor {

    private final FactionEnchantsPlugin plugin;

    public SplitSoulsCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§eUsage: §f/splitsouls <amount>");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cAmount must be a positive whole number.");
            return true;
        }

        if (amount <= 0) {
            player.sendMessage("§cAmount must be greater than zero.");
            return true;
        }

        ItemStack held = player.getInventory().getItemInMainHand();
        if (!SoulGemItem.isSoulGem(plugin, held) || SoulGemItem.isGenerator(plugin, held)) {
            player.sendMessage("§cYou must be holding a Soul Gem to split it.");
            return true;
        }

        int current = SoulGemItem.getCharges(plugin, held);
        if (amount >= current) {
            String fmtCurrent = NumberFormat.getNumberInstance(Locale.US).format(current);
            player.sendMessage("§cNot enough charges! Your gem has §e" + fmtCurrent + " §ccharges — split amount must be less than that.");
            return true;
        }

        // Reduce held gem
        SoulGemItem.setCharges(plugin, held, current - amount);
        player.getInventory().setItemInMainHand(held);

        // Give split gem
        ItemStack splitGem = SoulGemItem.create(plugin, amount);
        var leftovers = player.getInventory().addItem(splitGem);
        leftovers.values().forEach(l -> player.getWorld().dropItemNaturally(player.getLocation(), l));

        player.updateInventory();

        String fmtRemain = NumberFormat.getNumberInstance(Locale.US).format(current - amount);
        String fmtSplit  = NumberFormat.getNumberInstance(Locale.US).format(amount);
        player.sendMessage("§c✦ §7Soul Gem split! Held gem: §c" + fmtRemain + " §7charges. New gem: §c" + fmtSplit + " §7charges.");

        // Refresh action bar instantly
        plugin.getSoulManager().sendHeldGemHud(player);
        return true;
    }
}
