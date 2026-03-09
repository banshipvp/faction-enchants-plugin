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

    public static final String GUI_TITLE = "\u00a76\u00a7lEnchanter";

    /**
     * PDC keys placed on vanilla-enchant shop panes.
     * Initialised in the constructor so the plugin NamespacedKey is available.
     */
    public static NamespacedKey VANILLA_ENCH_KEY;
    public static NamespacedKey VANILLA_ENCH_LEVEL_KEY;

    private final FactionEnchantsPlugin plugin;

    public EnchanterCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        VANILLA_ENCH_KEY       = new NamespacedKey(plugin, "vanilla_ench_key");
        VANILLA_ENCH_LEVEL_KEY = new NamespacedKey(plugin, "vanilla_ench_level");
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
    // GUI layout  (54 slots = 6 rows × 9 cols)
    //
    //  Row 0  (0-8)   – black-glass border
    //  Row 1  (9-17)  – custom-tier books, 3 | gap | 3 (symmetric)
    //  Row 2  (18-26) – combat vanilla enchants
    //  Row 3  (27-35) – armor vanilla enchants
    //  Row 4  (36-44) – tools & bow vanilla enchants
    //  Row 5  (45-53) – black-glass border
    // ─────────────────────────────────────────────────────────────────────────
    public static void openEnchanterGUI(Player player, FactionEnchantsPlugin plugin) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);

        // ── Row 1: Custom tier books (symmetric 3 | gap | 3) ─────────────────
        gui.setItem(10, createTierPane(CustomEnchantment.EnchantTier.SIMPLE,
                Material.WHITE_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.simple-cost", 5)));
        gui.setItem(11, createTierPane(CustomEnchantment.EnchantTier.UNIQUE,
                Material.GREEN_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.unique-cost", 15)));
        gui.setItem(12, createTierPane(CustomEnchantment.EnchantTier.ELITE,
                Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.elite-cost", 30)));
        // slot 13 = center gap (border)
        gui.setItem(14, createTierPane(CustomEnchantment.EnchantTier.ULTIMATE,
                Material.YELLOW_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.ultimate-cost", 50)));
        gui.setItem(15, createTierPane(CustomEnchantment.EnchantTier.LEGENDARY,
                Material.ORANGE_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.legendary-cost", 100)));
        gui.setItem(16, createTierPane(CustomEnchantment.EnchantTier.SOUL,
                Material.RED_STAINED_GLASS_PANE,
                plugin.getConfig().getInt("enchanter.soul-cost", 200)));

        // ── Row 2: Combat enchants ────────────────────────────────────────────
        placeVanilla(gui, plugin, 19, "sharpness",          5, 20);
        placeVanilla(gui, plugin, 20, "smite",              5, 15);
        placeVanilla(gui, plugin, 21, "bane_of_arthropods", 5, 15);
        placeVanilla(gui, plugin, 22, "looting",            3, 25);
        placeVanilla(gui, plugin, 23, "fire_aspect",        2, 20);
        placeVanilla(gui, plugin, 24, "knockback",          2, 15);
        placeVanilla(gui, plugin, 25, "sweeping_edge",      3, 20);

        // ── Row 3: Armor / survival enchants ─────────────────────────────────
        placeVanilla(gui, plugin, 28, "protection",          4, 30);
        placeVanilla(gui, plugin, 29, "feather_falling",     4, 25);
        placeVanilla(gui, plugin, 30, "unbreaking",          3, 20);
        placeVanilla(gui, plugin, 31, "thorns",              3, 25);
        placeVanilla(gui, plugin, 32, "depth_strider",       3, 30);
        placeVanilla(gui, plugin, 33, "mending",             1, 50);
        placeVanilla(gui, plugin, 34, "respiration",         3, 20);

        // ── Row 4: Tools & bow enchants ───────────────────────────────────────
        placeVanilla(gui, plugin, 37, "efficiency",  5, 20);
        placeVanilla(gui, plugin, 38, "fortune",     3, 30);
        placeVanilla(gui, plugin, 39, "silk_touch",  1, 35);
        placeVanilla(gui, plugin, 40, "power",       5, 20);
        placeVanilla(gui, plugin, 41, "punch",       2, 20);
        placeVanilla(gui, plugin, 42, "flame",       1, 15);
        placeVanilla(gui, plugin, 43, "infinity",    1, 40);

        // ── Border fill ───────────────────────────────────────────────────────
        ItemStack border = makeBorder(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 54; i++) {
            if (gui.getItem(i) == null) gui.setItem(i, border);
        }

        player.openInventory(gui);
    }

    // ─────────────────────────────────────────────────────────────────────────

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
                "\u00a77for \u00a7a" + cost + "\u00a77 XP levels.",
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
                "\u00a77for \u00a7a" + cost + "\u00a77 XP levels.",
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
