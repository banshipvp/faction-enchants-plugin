package com.factionenchants.books;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.EnchantmentManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class BookManager {

    private final FactionEnchantsPlugin plugin;
    private final Random random = new Random();

    public BookManager(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        EnchantBook.init(plugin);
    }

    public ItemStack openRandomBook(Player player, CustomEnchantment.EnchantTier tier) {
        EnchantmentManager em = plugin.getEnchantmentManager();
        List<CustomEnchantment> enchants = em.getEnchantmentsByTier(tier);
        if (enchants.isEmpty()) return null;
        CustomEnchantment chosen = enchants.get(random.nextInt(enchants.size()));
        int level = 1 + random.nextInt(chosen.getMaxLevel());
        int successRate = rollSuccessRate(tier);
        int destroyRate = rollDestroyRate(tier);
        return EnchantBook.createSpecificBook(chosen, level, successRate, destroyRate);
    }

    public boolean combineBooks(Player player, ItemStack book1, ItemStack book2) {
        CombineData data = getCombineData(book1, book2);
        if (data == null) return false;

        int cost = getCombineCost(book1, book2);
        if (player.getLevel() < cost) {
            player.sendMessage("§cYou need §e" + cost + " XP levels §cto combine these books!");
            return false;
        }
        player.setLevel(player.getLevel() - cost);
        player.getInventory().addItem(EnchantBook.createSpecificBook(data.enchant, data.newLevel));
        return true;
    }

    public int getCombineCost(ItemStack book1, ItemStack book2) {
        CombineData data = getCombineData(book1, book2);
        if (data == null) return -1;

        int levelMultiplier = plugin.getConfig().getInt("alchemist.level-multiplier", 5);
        int tierBase = plugin.getConfig().getInt("alchemist.tier-base." + data.enchant.getTier().name(), defaultTierBase(data.enchant.getTier()));
        return Math.max(1, tierBase + ((data.level1 + data.level2) * Math.max(1, levelMultiplier)));
    }

    public ItemStack getCombinePreview(ItemStack book1, ItemStack book2) {
        CombineData data = getCombineData(book1, book2);
        if (data == null) return null;
        return EnchantBook.createSpecificBook(data.enchant, data.newLevel);
    }

    private CombineData getCombineData(ItemStack book1, ItemStack book2) {
        if (!EnchantBook.isEnchantBook(book1) || !EnchantBook.isEnchantBook(book2)) return null;
        if (EnchantBook.isRandomBook(book1) || EnchantBook.isRandomBook(book2)) return null;

        String id1 = EnchantBook.getEnchantId(book1);
        String id2 = EnchantBook.getEnchantId(book2);
        if (id1 == null || id2 == null || !id1.equals(id2)) return null;

        int level1 = EnchantBook.getEnchantLevel(book1);
        int level2 = EnchantBook.getEnchantLevel(book2);
        if (level1 <= 0 || level2 <= 0) return null;

        CustomEnchantment enchant = plugin.getEnchantmentManager().getEnchantment(id1);
        if (enchant == null) return null;
        if (level1 >= enchant.getMaxLevel() || level2 >= enchant.getMaxLevel()) return null;

        int newLevel = Math.min(enchant.getMaxLevel(), level1 + level2);
        return new CombineData(enchant, level1, level2, newLevel);
    }

    private int defaultTierBase(CustomEnchantment.EnchantTier tier) {
        return switch (tier) {
            case SIMPLE -> 5;
            case UNIQUE -> 10;
            case ELITE -> 20;
            case ULTIMATE -> 35;
            case LEGENDARY -> 55;
            case SOUL -> 80;
            case HEROIC -> 110;
            case MASTERY -> 150;
        };
    }

    private static class CombineData {
        private final CustomEnchantment enchant;
        private final int level1;
        private final int level2;
        private final int newLevel;

        private CombineData(CustomEnchantment enchant, int level1, int level2, int newLevel) {
            this.enchant = enchant;
            this.level1 = level1;
            this.level2 = level2;
            this.newLevel = newLevel;
        }
    }

    private int rollSuccessRate(CustomEnchantment.EnchantTier tier) {
        String basePath = "book-rates." + tier.name() + ".success";
        int min = plugin.getConfig().getInt(basePath + "-min", defaultSuccessMin(tier));
        int max = plugin.getConfig().getInt(basePath + "-max", defaultSuccessMax(tier));
        return randomBetweenNormalized(min, max);
    }

    private int rollDestroyRate(CustomEnchantment.EnchantTier tier) {
        String basePath = "book-rates." + tier.name() + ".destroy";
        int min = plugin.getConfig().getInt(basePath + "-min", defaultDestroyMin(tier));
        int max = plugin.getConfig().getInt(basePath + "-max", defaultDestroyMax(tier));
        return randomBetweenNormalized(min, max);
    }

    private int randomBetweenNormalized(int a, int b) {
        int min = Math.max(0, Math.min(100, Math.min(a, b)));
        int max = Math.max(0, Math.min(100, Math.max(a, b)));
        return min + random.nextInt(max - min + 1);
    }

    private int defaultSuccessMin(CustomEnchantment.EnchantTier tier) {
        return switch (tier) {
            case SIMPLE -> 85;
            case UNIQUE -> 80;
            case ELITE -> 70;
            case ULTIMATE -> 60;
            case LEGENDARY -> 50;
            case SOUL -> 45;
            case HEROIC -> 40;
            case MASTERY -> 35;
        };
    }

    private int defaultSuccessMax(CustomEnchantment.EnchantTier tier) {
        return switch (tier) {
            case SIMPLE -> 100;
            case UNIQUE -> 95;
            case ELITE -> 90;
            case ULTIMATE -> 85;
            case LEGENDARY -> 80;
            case SOUL -> 75;
            case HEROIC -> 70;
            case MASTERY -> 65;
        };
    }

    private int defaultDestroyMin(CustomEnchantment.EnchantTier tier) {
        return switch (tier) {
            case SIMPLE -> 0;
            case UNIQUE -> 2;
            case ELITE -> 5;
            case ULTIMATE -> 8;
            case LEGENDARY -> 10;
            case SOUL -> 12;
            case HEROIC -> 15;
            case MASTERY -> 20;
        };
    }

    private int defaultDestroyMax(CustomEnchantment.EnchantTier tier) {
        return switch (tier) {
            case SIMPLE -> 5;
            case UNIQUE -> 10;
            case ELITE -> 15;
            case ULTIMATE -> 20;
            case LEGENDARY -> 25;
            case SOUL -> 30;
            case HEROIC -> 35;
            case MASTERY -> 40;
        };
    }
}
