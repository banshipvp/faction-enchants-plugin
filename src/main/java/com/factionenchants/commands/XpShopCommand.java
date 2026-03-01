package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class XpShopCommand implements CommandExecutor {

    public static final String GUI_TITLE = "§6§lXP Shop";
    public static final String ITEM_DETONATE_PICKAXE = "detonate_pickaxe";
    public static final String ITEM_LAVA_FAT_BUCKET = "lava_fat_bucket";
    public static final String ITEM_WATER_FAT_BUCKET = "water_fat_bucket";
    public static final String ITEM_SELL_WAND = "sell_wand";
    public static final String ITEM_TNT_WAND = "tnt_wand";

    private static NamespacedKey SHOP_ITEM_ID_KEY;
    private static NamespacedKey SHOP_ITEM_COST_KEY;

    private final FactionEnchantsPlugin plugin;

    public XpShopCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        SHOP_ITEM_ID_KEY = new NamespacedKey(plugin, "xpshop_item_id");
        SHOP_ITEM_COST_KEY = new NamespacedKey(plugin, "xpshop_item_cost");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        openShop(player);
        return true;
    }

    public void openShop(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        gui.setItem(10, createShopDisplayItem(Material.DIAMOND_PICKAXE, "§e§lDetonate Pickaxe", ITEM_DETONATE_PICKAXE,
                getCost("xpshop.detonate-pickaxe", 10_000),
                "§7Comes with §5Detonate I"));

        gui.setItem(11, createShopDisplayItem(Material.LAVA_BUCKET, "§6§lLava Fat Bucket", ITEM_LAVA_FAT_BUCKET,
                getCost("xpshop.lava-fat-bucket", 15_000),
                "§7Useful for fast lava placement"));

        gui.setItem(12, createShopDisplayItem(Material.WATER_BUCKET, "§b§lWater Fat Bucket", ITEM_WATER_FAT_BUCKET,
                getCost("xpshop.water-fat-bucket", 15_000),
                "§7Useful for fast water placement"));

        gui.setItem(14, createShopDisplayItem(Material.BLAZE_ROD, "§a§lSell Wand", ITEM_SELL_WAND,
                getCost("xpshop.sell-wand", 50_000),
                "§7Right-click containers to sell"));

        gui.setItem(15, createShopDisplayItem(Material.STICK, "§c§lTNT Wand", ITEM_TNT_WAND,
                getCost("xpshop.tnt-wand", 50_000),
                "§7Right-click TNT storage blocks"));

        ItemStack border = makeBorder(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) gui.setItem(i, border);
        }

        player.openInventory(gui);
    }

    public int getCost(String path, int defaultCost) {
        return plugin.getConfig().getInt(path, defaultCost);
    }

    public ItemStack createPurchasedItem(String itemId) {
        return switch (itemId) {
            case ITEM_DETONATE_PICKAXE -> createDetonatePickaxe();
            case ITEM_LAVA_FAT_BUCKET -> createCustomItem(Material.LAVA_BUCKET, "§6§lLava Fat Bucket", "§7Purchased from XP Shop");
            case ITEM_WATER_FAT_BUCKET -> createCustomItem(Material.WATER_BUCKET, "§b§lWater Fat Bucket", "§7Purchased from XP Shop");
            case ITEM_SELL_WAND -> null;
            case ITEM_TNT_WAND -> null;
            default -> null;
        };
    }

    private ItemStack createDetonatePickaxe() {
        ItemStack pickaxe = createCustomItem(Material.DIAMOND_PICKAXE, "§e§lDetonate Pickaxe", "§7Comes with §5Detonate I");
        CustomEnchantment detonate = plugin.getEnchantmentManager().getEnchantment("detonate");
        if (detonate != null) {
            pickaxe = plugin.getEnchantmentManager().applyEnchantment(pickaxe, detonate, 1);
        }
        return pickaxe;
    }

    private ItemStack createCustomItem(Material material, String displayName, String loreLine) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(loreLine));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createShopDisplayItem(Material material, String name, String itemId, int cost, String featureLine) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(
                featureLine,
                "",
                "§eCost: §f" + NumberFormat.getNumberInstance(Locale.US).format(cost) + " XP",
                "§7Click to purchase"
        ));
        meta.getPersistentDataContainer().set(SHOP_ITEM_ID_KEY, PersistentDataType.STRING, itemId);
        meta.getPersistentDataContainer().set(SHOP_ITEM_COST_KEY, PersistentDataType.INTEGER, cost);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack makeBorder(Material material) {
        ItemStack pane = new ItemStack(material);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(" ");
        pane.setItemMeta(meta);
        return pane;
    }

    public static String getShopItemId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(SHOP_ITEM_ID_KEY, PersistentDataType.STRING);
    }

    public static Integer getShopItemCost(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(SHOP_ITEM_COST_KEY, PersistentDataType.INTEGER);
    }
}
