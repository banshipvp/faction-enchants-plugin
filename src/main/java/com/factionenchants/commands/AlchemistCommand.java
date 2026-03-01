package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AlchemistCommand implements CommandExecutor {

    public static final String GUI_TITLE = "\u00a75\u00a7lAlchemist";

    private final FactionEnchantsPlugin plugin;

    public AlchemistCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        openAlchemistGUI(player);
        return true;
    }

    public static void openAlchemistGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        ItemStack combine = new ItemStack(Material.ANVIL);
        ItemMeta cm = combine.getItemMeta();
        cm.setDisplayName("\u00a7aCombine Books");
        cm.setLore(Arrays.asList(
                "\u00a77Place two identical enchant books",
                "\u00a77in the side slots to combine them.",
                "",
                "\u00a7eClick to combine!"
        ));
        combine.setItemMeta(cm);
        gui.setItem(13, combine);

        ItemStack preview = new ItemStack(Material.BOOK);
        ItemMeta previewMeta = preview.getItemMeta();
        previewMeta.setDisplayName("§b§lCombine Preview");
        previewMeta.setLore(Arrays.asList(
            "§7Place books in slots 11 and 15",
            "§7to preview the result here."
        ));
        preview.setItemMeta(previewMeta);
        gui.setItem(22, preview);

        // Slots 11 and 15 are intentionally left empty for players to place books

        fillBorder(gui);
        player.openInventory(gui);
    }

    private static void fillBorder(Inventory inv) {
        ItemStack glass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);
        for (int i = 0; i < 27; i++) {
            if (i == 11 || i == 13 || i == 15 || i == 22) continue; // leave input/combine/preview slots
            inv.setItem(i, glass);
        }
    }

}
