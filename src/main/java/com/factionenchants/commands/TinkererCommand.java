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
import java.util.Collections;
import java.util.List;

public class TinkererCommand implements CommandExecutor {

    public static final String GUI_TITLE = "\u00a76\u00a7lTinkerer";
    public static final int GUI_SIZE = 54;
    public static final int CONFIRM_SLOT = 49;
    public static final int ADD_ALL_SLOT = 4;
    public static final List<Integer> DIVIDER_SLOTS = Collections.unmodifiableList(Arrays.asList(4, 13, 22, 31, 40, 49));
    public static final List<Integer> INPUT_SLOTS = Collections.unmodifiableList(Arrays.asList(
        0, 1, 2, 3,
        9, 10, 11, 12,
        18, 19, 20, 21,
        27, 28, 29, 30,
        36, 37, 38, 39,
        45, 46, 47, 48
    ));
    public static final List<Integer> OUTPUT_SLOTS = Collections.unmodifiableList(Arrays.asList(
        5, 6, 7, 8,
        14, 15, 16, 17,
        23, 24, 25, 26,
        32, 33, 34, 35,
        41, 42, 43, 44,
        50, 51, 52, 53
    ));

    private final FactionEnchantsPlugin plugin;

    public TinkererCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        openTinkererGUI(player);
        return true;
    }

    public static void openTinkererGUI(Player player, FactionEnchantsPlugin plugin) {
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, GUI_TITLE);

        ItemStack divider = makeBorder(Material.GRAY_STAINED_GLASS_PANE);
        for (int slot : DIVIDER_SLOTS) {
            gui.setItem(slot, divider);
        }

        ItemStack addAll = new ItemStack(Material.HOPPER);
        ItemMeta addAllMeta = addAll.getItemMeta();
        addAllMeta.setDisplayName("\u00a7b\u00a7lAdd All");
        addAllMeta.setLore(Arrays.asList(
            "\u00a77Move all valid enchant books/gear",
            "\u00a77from your inventory to input slots",
            "",
            "\u00a7bClick to add all"
        ));
        addAll.setItemMeta(addAllMeta);
        gui.setItem(ADD_ALL_SLOT, addAll);

        for (int slot : INPUT_SLOTS) {
            gui.setItem(slot, null);
        }

        for (int slot : OUTPUT_SLOTS) {
            gui.setItem(slot, null);
        }

        ItemStack confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName("\u00a7a\u00a7lConfirm Recycle");
        confirmMeta.setLore(Arrays.asList(
                "\u00a77Left side: place enchant books or gear",
                "\u00a77Right side: see preview output",
                "",
                "\u00a7aClick to confirm"
        ));
        confirm.setItemMeta(confirmMeta);
        gui.setItem(CONFIRM_SLOT, confirm);

        player.openInventory(gui);
    }

    private void openTinkererGUI(Player player) {
        openTinkererGUI(player, plugin);
    }

    private static ItemStack makeBorder(Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
}
