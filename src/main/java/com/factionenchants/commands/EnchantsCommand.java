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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantsCommand implements CommandExecutor {

    public static final String GUI_TITLE = "\u00a7b\u00a7lEnchantments";

    private final FactionEnchantsPlugin plugin;

    public EnchantsCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        openEnchantsGUI(player, CustomEnchantment.EnchantTier.SIMPLE);
        return true;
    }

    public void openEnchantsGUI(Player player, CustomEnchantment.EnchantTier tier) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE + " - " + tier.getDisplayName());

        // Navigation buttons in the bottom row
        gui.setItem(45, createNavigationButton(CustomEnchantment.EnchantTier.SIMPLE, Material.WHITE_STAINED_GLASS_PANE));
        gui.setItem(46, createNavigationButton(CustomEnchantment.EnchantTier.UNIQUE, Material.GREEN_STAINED_GLASS_PANE));
        gui.setItem(47, createNavigationButton(CustomEnchantment.EnchantTier.ELITE, Material.LIGHT_BLUE_STAINED_GLASS_PANE));
        gui.setItem(48, createNavigationButton(CustomEnchantment.EnchantTier.ULTIMATE, Material.YELLOW_STAINED_GLASS_PANE));
        gui.setItem(49, createNavigationButton(CustomEnchantment.EnchantTier.LEGENDARY, Material.ORANGE_STAINED_GLASS_PANE));
        gui.setItem(50, createNavigationButton(CustomEnchantment.EnchantTier.SOUL, Material.RED_STAINED_GLASS_PANE));
        gui.setItem(51, createNavigationButton(CustomEnchantment.EnchantTier.HEROIC, Material.MAGENTA_STAINED_GLASS_PANE));
        gui.setItem(52, createNavigationButton(CustomEnchantment.EnchantTier.MASTERY, Material.PURPLE_STAINED_GLASS_PANE));

        // Close button
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        closeMeta.setDisplayName("\u00a7c\u00a7lClose");
        closeButton.setItemMeta(closeMeta);
        gui.setItem(53, closeButton);

        // Get enchantments for this tier
        List<CustomEnchantment> tierEnchants = plugin.getEnchantmentManager().getEnchantmentsByTier(tier);

        // Display enchantments in the main area (slots 0-44)
        int slot = 0;
        for (CustomEnchantment enchant : tierEnchants) {
            if (slot >= 45) break; // Don't overflow the inventory
            gui.setItem(slot, createEnchantDisplay(enchant));
            slot++;
        }

        // Fill empty slots with border
        ItemStack border = makeBorder(Material.GRAY_STAINED_GLASS_PANE);
        for (int i = 0; i < 45; i++) {
            if (gui.getItem(i) == null) gui.setItem(i, border);
        }

        player.openInventory(gui);
    }

    private ItemStack createNavigationButton(CustomEnchantment.EnchantTier tier, Material mat) {
        ItemStack button = new ItemStack(mat);
        ItemMeta meta = button.getItemMeta();
        String color = "\u00a7" + tier.getColor();
        meta.setDisplayName(color + "\u00a7l" + tier.getDisplayName());
        meta.setLore(Arrays.asList(
                "\u00a77Click to view " + color + tier.getDisplayName() + "\u00a77 enchantments"
        ));
        // Store tier in button for navigation
        meta.getPersistentDataContainer().set(
                com.factionenchants.books.EnchantBook.BOOK_TIER_KEY,
                org.bukkit.persistence.PersistentDataType.STRING,
                tier.name()
        );
        button.setItemMeta(meta);
        return button;
    }

    private ItemStack createEnchantDisplay(CustomEnchantment enchant) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        String color = "\u00a7" + enchant.getTier().getColor();
        meta.setDisplayName(color + enchant.getDisplayName());

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("\u00a77Max Level: \u00a7e" + enchant.getMaxLevel());
        lore.add("\u00a77Applicable: \u00a7f" + enchant.getApplicableGearDisplay());

        // Add description if available
        String desc = enchant.getDescription();
        if (desc != null && !desc.isEmpty()) {
            lore.add("");
            lore.add("\u00a77" + desc);
        }

        // Add prerequisite info for Heroic enchants
        String prereq = enchant.getPrerequisiteEnchantId();
        if (prereq != null) {
            lore.add("");
            lore.add("\u00a76Requires: \u00a7e" + prereq);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack makeBorder(Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
}