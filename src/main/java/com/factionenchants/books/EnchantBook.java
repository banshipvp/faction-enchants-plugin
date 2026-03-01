package com.factionenchants.books;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.EnchantmentManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantBook {

    public static final int MAX_ENCHANTS_PER_ITEM = 9;

    public static NamespacedKey BOOK_TIER_KEY;
    public static NamespacedKey ENCHANT_ID_KEY;
    public static NamespacedKey ENCHANT_LEVEL_KEY;
    public static NamespacedKey ENCHANT_SUCCESS_RATE_KEY;
    public static NamespacedKey ENCHANT_DESTROY_RATE_KEY;

    public static void init(FactionEnchantsPlugin plugin) {
        BOOK_TIER_KEY = new NamespacedKey(plugin, "book_tier");
        ENCHANT_ID_KEY = new NamespacedKey(plugin, "enchant_id");
        ENCHANT_LEVEL_KEY = new NamespacedKey(plugin, "enchant_level");
        ENCHANT_SUCCESS_RATE_KEY = new NamespacedKey(plugin, "enchant_success_rate");
        ENCHANT_DESTROY_RATE_KEY = new NamespacedKey(plugin, "enchant_destroy_rate");
    }

    /** Mystery book — player right-clicks to reveal a random specific enchant book. */
    public static ItemStack createRandomBook(CustomEnchantment.EnchantTier tier) {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        String color = "\u00a7" + tier.getColor();
        String name = tier.getDisplayName();
        meta.setDisplayName(color + "\u00a7l" + name + " Enchant Book");
        meta.setLore(Arrays.asList(
                "\u00a77Right-click to reveal a random",
                color + name + "\u00a77 enchant!",
                "",
                "\u00a78Tier: " + color + name
        ));
        meta.getPersistentDataContainer().set(BOOK_TIER_KEY, PersistentDataType.STRING, tier.name());
        book.setItemMeta(meta);
        return book;
    }

    /** Specific enchant book — pick up and click onto gear to apply. */
    public static ItemStack createSpecificBook(CustomEnchantment enchant, int level) {
        return createSpecificBook(enchant, level, 100, 0);
    }

    public static ItemStack createSpecificBook(CustomEnchantment enchant, int level, int successRate, int destroyRate) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        String color = "\u00a7" + enchant.getTier().getColor();
        String name = enchant.getDisplayName();
        String roman = EnchantmentManager.toRoman(level);
        meta.setDisplayName(color + "\u00a7l" + name + " " + roman);
        List<String> lore = new ArrayList<>();
        // Description (supports \n line breaks)
        String desc = enchant.getDescription();
        if (!desc.isEmpty()) {
            for (String line : desc.split("\n")) {
                lore.add("\u00a77" + line);
            }
            lore.add("");
        }
        lore.add("\u00a78Tier: " + color + enchant.getTier().getDisplayName());
        lore.add("\u00a78Level: \u00a7f" + roman + " \u00a78/ \u00a7f" + EnchantmentManager.toRoman(enchant.getMaxLevel()));
        lore.add("\u00a78Applies to: \u00a7f" + enchant.getApplicableGearDisplay());
        lore.add("\u00a7aSuccess Rate: \u00a7f" + successRate + "%");
        lore.add("\u00a7cDestroy Rate: \u00a7f" + destroyRate + "%");
        lore.add("");
        lore.add("\u00a77Pick up and click onto your gear to apply.");
        meta.setLore(lore);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(BOOK_TIER_KEY, PersistentDataType.STRING, enchant.getTier().name());
        pdc.set(ENCHANT_ID_KEY, PersistentDataType.STRING, enchant.getId());
        pdc.set(ENCHANT_LEVEL_KEY, PersistentDataType.INTEGER, level);
        pdc.set(ENCHANT_SUCCESS_RATE_KEY, PersistentDataType.INTEGER, successRate);
        pdc.set(ENCHANT_DESTROY_RATE_KEY, PersistentDataType.INTEGER, destroyRate);
        book.setItemMeta(meta);
        return book;
    }

    public static boolean isEnchantBook(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(BOOK_TIER_KEY, PersistentDataType.STRING);
    }

    public static boolean isRandomBook(ItemStack item) {
        if (!isEnchantBook(item)) return false;
        return !item.getItemMeta().getPersistentDataContainer().has(ENCHANT_ID_KEY, PersistentDataType.STRING);
    }

    public static CustomEnchantment.EnchantTier getTier(ItemStack item) {
        if (!isEnchantBook(item)) return null;
        String tierStr = item.getItemMeta().getPersistentDataContainer().get(BOOK_TIER_KEY, PersistentDataType.STRING);
        try { return CustomEnchantment.EnchantTier.valueOf(tierStr); } catch (Exception e) { return null; }
    }

    public static String getEnchantId(ItemStack item) {
        if (!isEnchantBook(item)) return null;
        return item.getItemMeta().getPersistentDataContainer().get(ENCHANT_ID_KEY, PersistentDataType.STRING);
    }

    public static int getEnchantLevel(ItemStack item) {
        if (!isEnchantBook(item)) return -1;
        Integer lvl = item.getItemMeta().getPersistentDataContainer().get(ENCHANT_LEVEL_KEY, PersistentDataType.INTEGER);
        return lvl != null ? lvl : -1;
    }

    public static int getSuccessRate(ItemStack item) {
        if (!isEnchantBook(item)) return 100;
        Integer rate = item.getItemMeta().getPersistentDataContainer().get(ENCHANT_SUCCESS_RATE_KEY, PersistentDataType.INTEGER);
        return rate != null ? rate : 100;
    }

    public static int getDestroyRate(ItemStack item) {
        if (!isEnchantBook(item)) return 0;
        Integer rate = item.getItemMeta().getPersistentDataContainer().get(ENCHANT_DESTROY_RATE_KEY, PersistentDataType.INTEGER);
        return rate != null ? rate : 0;
    }
}
