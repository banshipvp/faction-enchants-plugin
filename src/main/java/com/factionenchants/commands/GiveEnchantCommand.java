package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.books.EnchantBook;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.EnchantmentManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GiveEnchantCommand implements CommandExecutor, TabCompleter {

    private final FactionEnchantsPlugin plugin;

    public GiveEnchantCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    // Usage:
    //   /fegive <enchant_id> <level>           — give to yourself
    //   /fegive <player> <enchant_id> <level>  — give to another player

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("factionenchants.give")) {
            sender.sendMessage("\u00a7cYou don't have permission to use this command.");
            return true;
        }

        Player target;
        String enchantId;
        int level;

        if (args.length == 2) {
            // /fegive <enchant_id> <level>
            if (!(sender instanceof Player)) {
                sender.sendMessage("Console must specify a player: /fegive <player> <enchant_id> <level>");
                return true;
            }
            target = (Player) sender;
            enchantId = args[0].toLowerCase();
            try { level = Integer.parseInt(args[1]); }
            catch (NumberFormatException e) { sender.sendMessage("\u00a7cLevel must be a number."); return true; }
        } else if (args.length == 3) {
            // /fegive <player> <enchant_id> <level>
            target = Bukkit.getPlayer(args[0]);
            if (target == null) { sender.sendMessage("\u00a7cPlayer \u00a7e" + args[0] + "\u00a7c is not online."); return true; }
            enchantId = args[1].toLowerCase();
            try { level = Integer.parseInt(args[2]); }
            catch (NumberFormatException e) { sender.sendMessage("\u00a7cLevel must be a number."); return true; }
        } else {
            sender.sendMessage("\u00a7eUsage: \u00a7f/fegive [player] <enchant_id> <level>");
            sender.sendMessage("\u00a77Example: \u00a7f/fegive vampiric 3");
            sender.sendMessage("\u00a77Example: \u00a7f/fegive Steve springs 2");
            listEnchants(sender);
            return true;
        }

        CustomEnchantment enchant = plugin.getEnchantmentManager().getEnchantment(enchantId);
        if (enchant == null) {
            sender.sendMessage("\u00a7cUnknown enchant: \u00a7e" + enchantId);
            listEnchants(sender);
            return true;
        }

        if (level < 1 || level > enchant.getMaxLevel()) {
            sender.sendMessage("\u00a7cLevel must be between \u00a7e1\u00a7c and \u00a7e" + enchant.getMaxLevel() + "\u00a7c for " + enchant.getDisplayName() + ".");
            return true;
        }

        ItemStack book = EnchantBook.createSpecificBook(enchant, level);
        target.getInventory().addItem(book);

        String color = "\u00a7" + enchant.getTier().getColor();
        target.sendMessage("\u00a7aYou received " + color + enchant.getDisplayName() + " " + EnchantmentManager.toRoman(level) + "\u00a7a!");
        if (sender != target) {
            sender.sendMessage("\u00a7aGave " + color + enchant.getDisplayName() + " " + EnchantmentManager.toRoman(level) + "\u00a7a to \u00a7e" + target.getName() + "\u00a7a.");
        }
        return true;
    }

    private void listEnchants(CommandSender sender) {
        sender.sendMessage("\u00a77Available enchants:");
        String list = plugin.getEnchantmentManager().getAllEnchantments().stream()
                .map(e -> "\u00a7" + e.getTier().getColor() + e.getId())
                .collect(Collectors.joining("\u00a77, "));
        sender.sendMessage(list);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // Could be a player name or enchant id
            String partial = args[0].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(partial)) completions.add(p.getName());
            }
            plugin.getEnchantmentManager().getAllEnchantments().stream()
                    .map(e -> e.getId())
                    .filter(id -> id.startsWith(partial))
                    .forEach(completions::add);
        } else if (args.length == 2) {
            // Could be enchant id (if arg[0] was a player) or level
            String partial = args[1].toLowerCase();
            plugin.getEnchantmentManager().getAllEnchantments().stream()
                    .map(e -> e.getId())
                    .filter(id -> id.startsWith(partial))
                    .forEach(completions::add);
            for (int i = 1; i <= 10; i++) {
                if (String.valueOf(i).startsWith(partial)) completions.add(String.valueOf(i));
            }
        } else if (args.length == 3) {
            // Level
            String partial = args[2];
            for (int i = 1; i <= 10; i++) {
                if (String.valueOf(i).startsWith(partial)) completions.add(String.valueOf(i));
            }
        }
        return completions;
    }
}
