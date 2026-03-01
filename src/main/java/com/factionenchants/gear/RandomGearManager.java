package com.factionenchants.gear;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.books.EnchantBook;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomGearManager {

    private final FactionEnchantsPlugin plugin;
    private final Random random = new Random();

    public RandomGearManager(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack generateRandomEnchantedGear(ItemStack baseItem) {
        int min = Math.max(1, plugin.getConfig().getInt("random-gear.min-enchants", 1));
        int max = Math.max(min, plugin.getConfig().getInt("random-gear.max-enchants", 4));
        return generateRandomEnchantedGear(baseItem, min, max);
    }

    public ItemStack generateRandomEnchantedGear(ItemStack baseItem, int minEnchants, int maxEnchants) {
        if (baseItem == null) return null;

        ItemStack result = baseItem.clone();
        List<CustomEnchantment> candidates = getApplicableEnchantments(result);
        if (candidates.isEmpty()) return result;

        int min = Math.max(1, minEnchants);
        int max = Math.max(min, maxEnchants);
        int targetCount = min + random.nextInt((max - min) + 1);
        targetCount = Math.min(targetCount, Math.min(candidates.size(), EnchantBook.MAX_ENCHANTS_PER_ITEM));

        Map<CustomEnchantment.EnchantTier, Integer> weights = getTierWeights();
        List<CustomEnchantment> pool = new ArrayList<>(candidates);

        for (int i = 0; i < targetCount; i++) {
            CustomEnchantment chosen = pickWeighted(pool, weights);
            if (chosen == null) break;

            int level = 1 + random.nextInt(chosen.getMaxLevel());
            result = plugin.getEnchantmentManager().applyEnchantment(result, chosen, level);
            pool.remove(chosen);
        }

        return result;
    }

    private List<CustomEnchantment> getApplicableEnchantments(ItemStack item) {
        List<CustomEnchantment> list = new ArrayList<>();
        for (CustomEnchantment enchantment : plugin.getEnchantmentManager().getAllEnchantments()) {
            if (enchantment.canApplyTo(item)) {
                list.add(enchantment);
            }
        }
        return list;
    }

    private Map<CustomEnchantment.EnchantTier, Integer> getTierWeights() {
        Map<CustomEnchantment.EnchantTier, Integer> weights = new HashMap<>();
        for (CustomEnchantment.EnchantTier tier : CustomEnchantment.EnchantTier.values()) {
            int defaultWeight = defaultWeight(tier);
            int configured = plugin.getConfig().getInt("random-gear.tier-weights." + tier.name(), defaultWeight);
            weights.put(tier, Math.max(0, configured));
        }
        return weights;
    }

    private int defaultWeight(CustomEnchantment.EnchantTier tier) {
        return switch (tier) {
            case SIMPLE -> 35;
            case UNIQUE -> 25;
            case ELITE -> 16;
            case ULTIMATE -> 10;
            case LEGENDARY -> 7;
            case SOUL -> 4;
            case HEROIC -> 2;
            case MASTERY -> 1;
        };
    }

    private CustomEnchantment pickWeighted(List<CustomEnchantment> pool, Map<CustomEnchantment.EnchantTier, Integer> weights) {
        if (pool.isEmpty()) return null;

        int total = 0;
        for (CustomEnchantment enchantment : pool) {
            total += Math.max(0, weights.getOrDefault(enchantment.getTier(), 0));
        }

        if (total <= 0) {
            return pool.get(random.nextInt(pool.size()));
        }

        int roll = random.nextInt(total);
        int running = 0;
        for (CustomEnchantment enchantment : pool) {
            running += Math.max(0, weights.getOrDefault(enchantment.getTier(), 0));
            if (roll < running) {
                return enchantment;
            }
        }

        return pool.get(pool.size() - 1);
    }
}
