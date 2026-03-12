package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.books.EnchantBook;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class EnchanterCommand implements CommandExecutor {

    public static final String GUI_TITLE        = "\u00a76\u00a7lEnchanter";
    public static final String VANILLA_GUI_TITLE = "\u00a76\u00a7lVanilla Enchants";

    /**
     * PDC keys placed on vanilla-enchant shop panes.
     * Initialised in the constructor so the plugin NamespacedKey is available.
     */
    public static NamespacedKey VANILLA_ENCH_KEY;
    public static NamespacedKey VANILLA_ENCH_LEVEL_KEY;
    public static NamespacedKey VANILLA_ENCHANTER_BTN_KEY;

    private final FactionEnchantsPlugin plugin;

    public EnchanterCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        VANILLA_ENCH_KEY          = new NamespacedKey(plugin, "vanilla_ench_key");
        VANILLA_ENCH_LEVEL_KEY    = new NamespacedKey(plugin, "vanilla_ench_level");
        VANILLA_ENCHANTER_BTN_KEY = new NamespacedKey(plugin, "vanilla_enchanter_btn");
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

    // ─────────────────────────────────────────────────────────────────────────
    // Main Enchanter GUI (27 slots = 3 rows × 9 cols)
    //
    //  Row 0  (0-8)   – black-glass border
    //  Row 1  (9-17)  – Vanilla | Simple | Unique | Elite | Ultimate | Legendary | Soul
    //  Row 2  (18-26) – black-glass border
    // ─────────────────────────────────────────────────────────────────────────
    public static void openEnchanterGUI(Player player, FactionEnchantsPlugin plugin) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        // Row 1: Vanilla + 6 custom tiers (slots 10-16)
        gui.setItem(10, createVanillaPane(plugin));
        gui.setItem(11, createTierPane(CustomEnchantment.EnchantTier.SIMPLE,
                Material.WHITE_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.simple-cost", 5)));
        gui.setItem(12, createTierPane(CustomEnchantment.EnchantTier.UNIQUE,
                Material.GREEN_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.unique-cost", 15)));
        gui.setItem(13, createTierPane(CustomEnchantment.EnchantTier.ELITE,
                Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.elite-cost", 30)));
        gui.setItem(14, createTierPane(CustomEnchantment.EnchantTier.ULTIMATE,
                Material.YELLOW_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.ultimate-cost", 50)));
        gui.setItem(15, createTierPane(CustomEnchantment.EnchantTier.LEGENDARY,
                Material.ORANGE_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.legendary-cost", 100)));
        gui.setItem(16, createTierPane(CustomEnchantment.EnchantTier.SOUL,
                Material.RED_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.soul-cost", 200)));

        // Border fill
        ItemStack border = makeBorder(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 27; i++) {
            if (gui.getItem(i) == null) gui.setItem(i, border);
        }

        player.openInventory(gui);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Vanilla Enchanter Sub-GUI (54 slots = 6 rows × 9 cols)
    // ─────────────────────────────────────────────────────────────────────────
    public static void openVanillaEnchanterGUI(Player player, FactionEnchantsPlugin plugin) {
        Inventory gui = Bukkit.createInventory(null, 54, VANILLA_GUI_TITLE);

        // Row 1: Combat enchants (slots 10-16)
        placeVanilla(gui, plugin, 10, "sharpness",          5, 20);
        placeVanilla(gui, plugin, 11, "smite",              5, 15);
        placeVanilla(gui, plugin, 12, "bane_of_arthropods", 5, 15);
        placeVanilla(gui, plugin, 13, "looting",            3, 25);
        placeVanilla(gui, plugin, 14, "fire_aspect",        2, 20);
        placeVanilla(gui, plugin, 15, "knockback",          2, 15);
        placeVanilla(gui, plugin, 16, "sweeping_edge",      3, 20);

        // Row 2: Armor / survival enchants (slots 19-25)
        placeVanilla(gui, plugin, 19, "protection",         4, 30);
        placeVanilla(gui, plugin, 20, "feather_falling",    4, 25);
        placeVanilla(gui, plugin, 21, "unbreaking",         3, 20);
        placeVanilla(gui, plugin, 22, "thorns",             3, 25);
        placeVanilla(gui, plugin, 23, "depth_strider",      3, 30);
        placeVanilla(gui, plugin, 24, "mending",            1, 50);
        placeVanilla(gui, plugin, 25, "respiration",        3, 20);

        // Row 3: Tools & bow enchants (slots 28-34)
        placeVanilla(gui, plugin, 28, "efficiency",  5, 20);
        placeVanilla(gui, plugin, 29, "fortune",     3, 30);
        placeVanilla(gui, plugin, 30, "silk_touch",  1, 35);
        placeVanilla(gui, plugin, 31, "power",       5, 20);
        placeVanilla(gui, plugin, 32, "punch",       2, 20);
        placeVanilla(gui, plugin, 33, "flame",       1, 15);
        placeVanilla(gui, plugin, 34, "infinity",    1, 40);

        // Back button (slot 45)
        ItemStack back = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("\u00a7c\u00a7l\u00ab Back to Enchanter");
        back.setItemMeta(backMeta);
        gui.setItem(45, back);

        // Border fill
        ItemStack border = makeBorder(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 54; i++) {
            if (gui.getItem(i) == null) gui.setItem(i, border);
        }

        player.openInventory(gui);
    }

    // ─────────────────────────────────────────────────────────────────────────

    private static ItemStack createVanillaPane(FactionEnchantsPlugin plugin) {
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName("\u00a77\u00a7lVanilla Enchants");
        meta.setLore(Arrays.asList(
                "",
                "\u00a77Click to browse & purchase",
                "\u00a77vanilla enchant books.",
                ""
        ));
        meta.getPersistentDataContainer().set(
                VANILLA_ENCHANTER_BTN_KEY, PersistentDataType.BYTE, (byte) 1);
        pane.setItemMeta(meta);
        return pane;
    }

    private static void placeVanilla(Inventory gui, FactionEnchantsPlugin plugin,
                                     int slot, String enchKey, int level, int defaultCost) {
        Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(enchKey));
        if (ench == null) return;
        int cost = plugin.getConfig().getInt("enchanter.vanilla." + enchKey + "-cost", defaultCost);
        gui.setItem(slot, createVanillaPane(ench, level, cost));
    }

    private static ItemStack createVanillaPane(Enchantment ench, int level, int cost) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta esm = (EnchantmentStorageMeta) book.getItemMeta();
        esm.addStoredEnchant(ench, level, true);
        esm.setDisplayName("\u00a7f\u00a7l" + prettyKey(ench.getKey().getKey()) + " " + toRoman(level));
        esm.setLore(Arrays.asList(
                "",
                "\u00a77Click to purchase",
                "\u00a77for \u00a7a" + cost + "\u00a77 XP.",
                ""
        ));
        esm.getPersistentDataContainer().set(VANILLA_ENCH_KEY,       PersistentDataType.STRING,  ench.getKey().getKey());
        esm.getPersistentDataContainer().set(VANILLA_ENCH_LEVEL_KEY, PersistentDataType.INTEGER, level);
        book.setItemMeta(esm);
        return book;
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
                "\u00a77for \u00a7a" + cost + "\u00a77 XP.",
                "",
                "\u00a78Right-click the book to reveal",
                "\u00a78your random enchant!"
        ));
        meta.getPersistentDataContainer().set(
                EnchantBook.BOOK_TIER_KEY,
                PersistentDataType.STRING,
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

    /** Converts a snake_case key to Title Case (e.g. "bane_of_arthropods" → "Bane Of Arthropods"). */
    public static String prettyKey(String key) {
        String[] parts = key.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            if (!sb.isEmpty()) sb.append(' ');
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
        }
        return sb.toString();
    }

    /** Roman-numeral helper (mirrors EnchantmentManager.toRoman). */
    public static String toRoman(int n) {
        return switch (n) {
            case 1 -> "I"; case 2 -> "II"; case 3 -> "III";
            case 4 -> "IV"; case 5 -> "V";  case 6 -> "VI";
            case 7 -> "VII"; case 8 -> "VIII"; case 9 -> "IX";
            case 10 -> "X"; default -> String.valueOf(n);
        };
    }

    private void openEnchanterGUI(Player player) {
        openEnchanterGUI(player, plugin);
    }
}
