package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
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

public class EnchanterCommand implements CommandExecutor {

    public static final String GUI_TITLE = "\u00a76\u00a7lEnchanter";

    private final FactionEnchantsPlugin plugin;

    public EnchanterCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        openEnchanterGUI(player);
        return true;
    }

    public static void openEnchanterGUI(Player player, FactionEnchantsPlugin plugin) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        // Row of colored glass panes - one per tier
        gui.setItem(10, createTierPane(CustomEnchantment.EnchantTier.SIMPLE,
                Material.WHITE_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.simple-cost", 5)));
        gui.setItem(11, createTierPane(CustomEnchantment.EnchantTier.UNIQUE,
                Material.GREEN_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.unique-cost", 15)));
        gui.setItem(12, createTierPane(CustomEnchantment.EnchantTier.ELITE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.elite-cost", 30)));
        gui.setItem(13, createTierPane(CustomEnchantment.EnchantTier.ULTIMATE,
                Material.YELLOW_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.ultimate-cost", 50)));
        gui.setItem(14, createTierPane(CustomEnchantment.EnchantTier.LEGENDARY,
                Material.ORANGE_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.legendary-cost", 100)));
        gui.setItem(15, createTierPane(CustomEnchantment.EnchantTier.SOUL,
                Material.RED_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.soul-cost", 200)));

        // Fill remaining slots with border
        ItemStack border = makeBorder(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 27; i++) {
            if (gui.getItem(i) == null) gui.setItem(i, border);
        }

        player.openInventory(gui);
    }

    private static ItemStack createTierPane(CustomEnchantment.EnchantTier tier, Material mat, int cost) {
        ItemStack pane = new ItemStack(mat);
        ItemMeta meta = pane.getItemMeta();
        String color = "\u00a7" + tier.getColor();
        String name = tier.getDisplayName();
        meta.setDisplayName(color + "\u00a7l" + name + " Enchant Book");
        meta.setLore(Arrays.asList(
                "",
                "\u00a77Click to purchase a",
                color + name + "\u00a77 mystery book",
                "\u00a77for \u00a7a" + cost + "\u00a77 XP levels.",
                "",
                "\u00a78Right-click the book to reveal",
                "\u00a78your random enchant!"
        ));
        // Store tier in pane so BookListener knows which tier was clicked
        meta.getPersistentDataContainer().set(
                com.factionenchants.books.EnchantBook.BOOK_TIER_KEY,
                org.bukkit.persistence.PersistentDataType.STRING,
                tier.name()
        );
        pane.setItemMeta(meta);
        return pane;
    }

    private static ItemStack makeBorder(Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    private void openEnchanterGUI(Player player) {
        openEnchanterGUI(player, plugin);
    }
}
