package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.items.NameTagItem;
import com.factionenchants.items.SoulGemItem;
import com.factionenchants.items.TransmogScrollItem;
import com.factionenchants.items.WhiteScrollItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import org.bukkit.persistence.PersistentDataType;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class XpShopCommand implements CommandExecutor {

    public static final String GUI_TITLE = "§6§lXP Shop";
    public static final String ITEM_DETONATE_PICKAXE = "detonate_pickaxe";
    public static final String ITEM_LAVA_FAT_BUCKET = "lava_fat_bucket";
    public static final String ITEM_WATER_FAT_BUCKET = "water_fat_bucket";
    public static final String ITEM_SELL_WAND = "sell_wand";
    public static final String ITEM_TNT_WAND = "tnt_wand";
    public static final String ITEM_BLACK_SCROLL = "black_scroll";
    public static final String ITEM_REROLL_SCROLL = "reroll_scroll";
    public static final String ITEM_COLLECTION_CHEST = "collection_chest";
    public static final String ITEM_TRAPPED_COLLECTION_CHEST = "trapped_collection_chest";
    public static final String ITEM_NAME_TAG              = "name_tag";
    public static final String ITEM_WHITE_SCROLL           = "white_scroll";
    public static final String ITEM_DEPTH_STRIDER_BOOK     = "depth_strider_book";
    public static final String ITEM_DIAMOND_PICKAXE_SET    = "diamond_pickaxe_set";
    public static final String ITEM_SOUL_GEM               = "xpshop_soul_gem";
    public static final String ITEM_RAND_SCROLL_SIMPLE     = "rand_scroll_simple";
    public static final String ITEM_RAND_SCROLL_UNIQUE     = "rand_scroll_unique";
    public static final String ITEM_RAND_SCROLL_ELITE      = "rand_scroll_elite";
    public static final String ITEM_RAND_SCROLL_ULTIMATE   = "rand_scroll_ultimate";
    public static final String ITEM_RAND_SCROLL_LEGENDARY  = "rand_scroll_legendary";
    public static final String ITEM_RAND_SCROLL_GODLY      = "rand_scroll_godly";
    public static final String ITEM_TRANSMOG_SCROLL        = "transmog_scroll";

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

        // Row 1: Utility Items
        gui.setItem(10, createShopDisplayItem(Material.DIAMOND_PICKAXE, "§e§lDetonate Pickaxe", ITEM_DETONATE_PICKAXE,
                getCost("xpshop.detonate-pickaxe", 10_000),
                "§7Comes with §5Detonate I"));

        gui.setItem(11, createShopDisplayItem(Material.LAVA_BUCKET, "§6§lLava Fat Bucket", ITEM_LAVA_FAT_BUCKET,
                getCost("xpshop.lava-fat-bucket", 15_000),
                "§7Useful for fast lava placement"));

        gui.setItem(12, createShopDisplayItem(Material.WATER_BUCKET, "§b§lWater Fat Bucket", ITEM_WATER_FAT_BUCKET,
                getCost("xpshop.water-fat-bucket", 15_000),
                "§7Can only be refilled with §bwater"));

        gui.setItem(13, createShopDisplayItem(Material.PAPER, "§e§lTransmog Scroll", ITEM_TRANSMOG_SCROLL,
                getCost("xpshop.transmog-scroll", 40_000),
                "§7Sorts enchants by rarity on gear"));

        // Row 2: Wands
        gui.setItem(14, createShopDisplayItem(Material.BLAZE_ROD, "§a§lSell Wand", ITEM_SELL_WAND,
                getCost("xpshop.sell-wand", 50_000),
                "§7Right-click containers to sell"));

        gui.setItem(15, createShopDisplayItem(Material.STICK, "§c§lTNT Wand", ITEM_TNT_WAND,
                getCost("xpshop.tnt-wand", 50_000),
                "§7Right-click TNT storage blocks"));

        gui.setItem(16, createShopDisplayItem(Material.INK_SAC, "§8§l⚫ Black Scroll", ITEM_BLACK_SCROLL,
                getCost("xpshop.black-scroll", 20_000),
                "§7Extract enchants from gear"));

        // Row 3: Scrolls & Chests
        gui.setItem(19, createShopDisplayItem(Material.PAPER, "§b§l✦ Reroll Scroll", ITEM_REROLL_SCROLL,
                getCost("xpshop.reroll-scroll", 25_000),
                "§7Reroll enchant percentages"));

        gui.setItem(20, createShopDisplayItem(Material.CHEST, "§a§lCollection Chest", ITEM_COLLECTION_CHEST,
                getCost("xpshop.collection-chest", 100_000),
                "§7Auto-collect mob drops"));

        gui.setItem(21, createShopDisplayItem(Material.TRAPPED_CHEST, "§c§lTrapped Collection Chest", ITEM_TRAPPED_COLLECTION_CHEST,
                getCost("xpshop.trapped-collection-chest", 110_000),
                "§7Auto-collect with pressure"));

        gui.setItem(22, createShopDisplayItem(Material.NAME_TAG, "§6§lName Tag", ITEM_NAME_TAG,
                getCost("xpshop.name-tag", 30_000),
                "§7Rename any item with custom colours"));

        gui.setItem(23, createShopDisplayItem(Material.MAP, "§f§lWhite Scroll", ITEM_WHITE_SCROLL,
                getCost("xpshop.white-scroll", 40_000),
                "§7Protect gear from book destruction"));

        gui.setItem(24, createShopDisplayItem(Material.ENCHANTED_BOOK, "§3§lDepth Strider III Book", ITEM_DEPTH_STRIDER_BOOK,
                getCost("xpshop.depth-strider-book", 50_000),
                "§7Depth Strider III enchant book"));

        gui.setItem(25, createShopDisplayItem(Material.DIAMOND_PICKAXE, "§b§lDiamond Pickaxe", ITEM_DIAMOND_PICKAXE_SET,
                getCost("xpshop.diamond-pickaxe-set", 15_000),
                "§7Efficiency V · Unbreaking III · Silk Touch I"));

        gui.setItem(26, createShopDisplayItem(Material.EMERALD, "§c§l✦ Soul Gem", ITEM_SOUL_GEM,
                getCost("xpshop.soul-gem", 75_000),
                "§71000 charges · Enables SOUL enchants"));

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
            case ITEM_LAVA_FAT_BUCKET -> {
                ItemStack lavaItem = createCustomItem(Material.LAVA_BUCKET, "§6§lLava Fat Bucket", "§7Can only be refilled with §4lava");
                org.bukkit.inventory.meta.ItemMeta lavaMeta = lavaItem.getItemMeta();
                lavaMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "lava_fat_bucket"), PersistentDataType.BYTE, (byte) 1);
                lavaItem.setItemMeta(lavaMeta);
                yield lavaItem;
            }
            case ITEM_WATER_FAT_BUCKET -> {
                ItemStack waterItem = createCustomItem(Material.WATER_BUCKET, "§b§lWater Fat Bucket", "§7Can only be refilled with §bwater");
                org.bukkit.inventory.meta.ItemMeta waterMeta = waterItem.getItemMeta();
                waterMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "water_fat_bucket"), PersistentDataType.BYTE, (byte) 1);
                waterItem.setItemMeta(waterMeta);
                yield waterItem;
            }
            case ITEM_SELL_WAND -> createSellWand();
            case ITEM_TNT_WAND -> createTntWand();
            case ITEM_BLACK_SCROLL -> createBlackScroll();
            case ITEM_REROLL_SCROLL -> createRerollScroll();
            case ITEM_COLLECTION_CHEST -> createCollectionChest(false);
            case ITEM_TRAPPED_COLLECTION_CHEST -> createCollectionChest(true);
            case ITEM_NAME_TAG           -> NameTagItem.create(plugin);
            case ITEM_WHITE_SCROLL        -> WhiteScrollItem.create(plugin);
            case ITEM_DEPTH_STRIDER_BOOK  -> createDepthStriderBook();
            case ITEM_DIAMOND_PICKAXE_SET -> createEnchantedDiamondPickaxe();
            case ITEM_SOUL_GEM            -> SoulGemItem.create(plugin, 1000);
            case ITEM_RAND_SCROLL_SIMPLE   -> com.factionenchants.items.RandomizationScrollItem.createSimple(plugin);
            case ITEM_RAND_SCROLL_UNIQUE   -> com.factionenchants.items.RandomizationScrollItem.createUnique(plugin);
            case ITEM_RAND_SCROLL_ELITE    -> com.factionenchants.items.RandomizationScrollItem.createElite(plugin);
            case ITEM_RAND_SCROLL_ULTIMATE -> com.factionenchants.items.RandomizationScrollItem.createUltimate(plugin);
            case ITEM_RAND_SCROLL_LEGENDARY-> com.factionenchants.items.RandomizationScrollItem.createLegendary(plugin);
            case ITEM_RAND_SCROLL_GODLY    -> com.factionenchants.items.RandomizationScrollItem.createGodly(plugin);
            case ITEM_TRANSMOG_SCROLL      -> TransmogScrollItem.create(plugin);
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
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(material);
        }
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(loreLine));
            item.setItemMeta(meta);
        }
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

    private ItemStack createDepthStriderBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        org.bukkit.enchantments.Enchantment depthStrider =
                org.bukkit.enchantments.Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft("depth_strider"));
        if (depthStrider != null) {
            meta.addStoredEnchant(depthStrider, 3, true);
        }
        meta.setDisplayName("§3§lDepth Strider III");
        meta.setLore(Arrays.asList(
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Increases movement speed",
                "§7while in water.",
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Purchased from XP Shop"
        ));
        book.setItemMeta(meta);
        return book;
    }

    private ItemStack createEnchantedDiamondPickaxe() {
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = pickaxe.getItemMeta();
        meta.setDisplayName("§b§lDiamond Pickaxe");
        meta.setLore(Arrays.asList(
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Efficiency V",
                "§7Unbreaking III",
                "§7Silk Touch I",
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Purchased from XP Shop"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        pickaxe.setItemMeta(meta);
        addVanillaEnchant(pickaxe, "efficiency",  5);
        addVanillaEnchant(pickaxe, "unbreaking",  3);
        addVanillaEnchant(pickaxe, "silk_touch",  1);
        return pickaxe;
    }

    /** Safely adds a vanilla enchantment by minecraft key. */
    private void addVanillaEnchant(ItemStack item, String key, int level) {
        org.bukkit.enchantments.Enchantment ench =
                org.bukkit.enchantments.Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft(key));
        if (ench != null) item.addUnsafeEnchantment(ench, level);
    }

    private ItemStack makeBorder(Material material) {
        ItemStack pane = new ItemStack(material);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(" ");
        pane.setItemMeta(meta);
        return pane;
    }

    private ItemStack createCollectionChest(boolean trapped) {
        Material material = trapped ? Material.TRAPPED_CHEST : Material.CHEST;
        String name = trapped ? "§c§lTrapped Collection Chest" : "§a§lCollection Chest";
        
        // SimpleFactionsRaiding detects collection chests by display name only
        // PDC is set on the BLOCK when placed, not on the item
        ItemStack item = createCustomItem(material, name, "§7Place to collect mob drops");
        return item;
    }

    private ItemStack createBlackScroll() {
        int successRate = 25 + new Random().nextInt(76); // 25-100%
        ItemStack scroll = new ItemStack(Material.INK_SAC);
        ItemMeta meta = scroll.getItemMeta();

        meta.setDisplayName("§8§l⚫ Black Scroll §l⚫");
        meta.setLore(Arrays.asList(
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Extract an enchant from",
                "§7enchanted gear or armor",
                "§7",
                "§eSuccess Rate: §f" + successRate + "%",
                "§cDestroy Rate: §f100%",
                "§7",
                "§eRight-click gear to extract",
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"
        ));

        NamespacedKey blackScrollKey = new NamespacedKey(plugin, "black_scroll");
        NamespacedKey blackScrollSuccessKey = new NamespacedKey(plugin, "black_scroll_success");
        meta.getPersistentDataContainer().set(blackScrollKey, PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer().set(blackScrollSuccessKey, PersistentDataType.INTEGER, successRate);

        scroll.setItemMeta(meta);
        return scroll;
    }

    private ItemStack createRerollScroll() {
        ItemStack scroll = new ItemStack(Material.PAPER);
        ItemMeta meta = scroll.getItemMeta();

        String tier = "ELITE";
        String color = "§b";
        
        meta.setDisplayName(color + "§l✦ " + tier + " Reroll Scroll §l✦");
        meta.setLore(Arrays.asList(
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Reroll success/destroy rates",
                "§7on enchanted books",
                "§7",
                "§eTarget: " + color + tier + " §eor lower",
                "§7",
                "§eRight-click on a book to apply",
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"
        ));

        NamespacedKey rerollScrollKey = new NamespacedKey(plugin, "reroll_scroll");
        NamespacedKey rerollTierKey = new NamespacedKey(plugin, "reroll_tier");
        meta.getPersistentDataContainer().set(rerollScrollKey, PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer().set(rerollTierKey, PersistentDataType.STRING, tier);

        scroll.setItemMeta(meta);
        return scroll;
    }

    private ItemStack createTntWand() {
        ItemStack wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName("§c§lTNT Wand");
        meta.setLore(List.of(
            "§7Left-click a chest/container",
            "§7to instantly deposit all TNT",
            "§7into your faction TNT bank.",
            "§8simplefactions:tnt_wand"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        wand.setItemMeta(meta);
        return wand;
    }

    private ItemStack createSellWand() {
        ItemStack wand = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName("§6§lSell Wand");
        meta.setLore(List.of(
            "§7Left-click a chest/container",
            "§7to sell all items inside.",
            "§8simplefactions:sell_wand"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        wand.setItemMeta(meta);
        return wand;
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
