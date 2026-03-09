package com.factionenchants.books;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Random;

public class DustManager {

    private static NamespacedKey DUST_TIER_KEY;
    private static NamespacedKey DUST_PERCENTAGE_KEY;
    private static NamespacedKey RANDOM_DUST_TOKEN_KEY;
    private static NamespacedKey RANDOM_DUST_DUD_KEY;
    private static NamespacedKey STORED_XP_BOTTLE_KEY;
    private final FactionEnchantsPlugin plugin;
    private final Random random = new Random();

    public DustManager(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    public static void init(FactionEnchantsPlugin plugin) {
        DUST_TIER_KEY = new NamespacedKey(plugin, "dust_tier");
        DUST_PERCENTAGE_KEY = new NamespacedKey(plugin, "dust_percentage");
        RANDOM_DUST_TOKEN_KEY = new NamespacedKey(plugin, "random_dust_token");
        RANDOM_DUST_DUD_KEY = new NamespacedKey(plugin, "random_dust_dud");
        STORED_XP_BOTTLE_KEY = new NamespacedKey(plugin, "stored_xp_bottle");
    }

    /**
     * Process an enchant book → random dust token (fire charge)
     */
    public ItemStack processEnchantBook(ItemStack book) {
        if (!EnchantBook.isEnchantBook(book) || EnchantBook.isRandomBook(book)) return null;
        
        String enchantId = EnchantBook.getEnchantId(book);
        CustomEnchantment enchant = plugin.getEnchantmentManager().getEnchantment(enchantId);
        if (enchant == null) return null;

        CustomEnchantment.EnchantTier tier = enchant.getTier();

        int totalPercent = random.nextInt(11); // 0–10%

        int dudChance = Math.max(0, Math.min(100, plugin.getConfig().getInt("random-dust.dud-chance", 30)));
        boolean dud = random.nextInt(100) < dudChance;
        return createRandomTierDustToken(tier, totalPercent, dud);
    }

    public int getMaxRandomDustPercentFromSuccessRate(int successRate) {
        int safeRate = Math.max(0, Math.min(100, successRate));
        int cap = Math.max(1, plugin.getConfig().getInt("random-dust.percent-cap", 15));

        int maxPercent;
        if (safeRate <= 10) {
            maxPercent = (int) Math.round(safeRate * 0.3);
        } else {
            maxPercent = 3 + (int) Math.round((safeRate - 10) * (12.0 / 90.0));
        }

        return Math.max(0, Math.min(cap, maxPercent));
    }

    public int getBottleCountForXp(int xpAmount) {
        int xpPerBottle = Math.max(1, plugin.getConfig().getInt("tinkerer.xp-per-bottle", 10));
        return Math.max(1, (int) Math.ceil(xpAmount / (double) xpPerBottle));
    }

    public ItemStack createStoredXpBottle(int xpAmount) {
        int safeXp = Math.max(1, xpAmount);
        ItemStack bottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = bottle.getItemMeta();
        meta.setDisplayName("§a§lStored XP Bottle");
        meta.setLore(Arrays.asList(
                "§7Contains: §e" + safeXp + " XP",
                "§7Right-click to redeem."
        ));
        meta.getPersistentDataContainer().set(STORED_XP_BOTTLE_KEY, PersistentDataType.INTEGER, safeXp);
        bottle.setItemMeta(meta);
        return bottle;
    }

    public boolean isStoredXpBottle(ItemStack item) {
        if (item == null || item.getType() != Material.EXPERIENCE_BOTTLE || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(STORED_XP_BOTTLE_KEY, PersistentDataType.INTEGER);
    }

    public int getStoredXpFromBottle(ItemStack item) {
        if (!isStoredXpBottle(item)) return 0;
        Integer value = item.getItemMeta().getPersistentDataContainer().get(STORED_XP_BOTTLE_KEY, PersistentDataType.INTEGER);
        return value == null ? 0 : Math.max(0, value);
    }

    /**
     * Process enchanted gear → XP amount
     */
    public int processEnchantedGear(ItemStack gear) {
        if (!canRecycleForXp(gear)) return 0;

        int baseXp = plugin.getConfig().getInt("tinkerer.gear-base-xp", 100);
        int vanillaEnchantPoint = plugin.getConfig().getInt("tinkerer.vanilla-enchant-point", 75);

        var enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(gear);
        int totalXP = 0;
        for (var entry : enchants.entrySet()) {
            CustomEnchantment enchant = entry.getKey();
            int level = entry.getValue();

            int enchantValue = switch (enchant.getTier()) {
                case SIMPLE -> 50;
                case UNIQUE -> 100;
                case ELITE -> 200;
                case ULTIMATE -> 400;
                case LEGENDARY -> 800;
                case SOUL -> 1500;
                case HEROIC -> 2500;
                case MASTERY -> 5000;
            };

            totalXP += enchantValue * level;
        }

        if (!gear.getEnchantments().isEmpty()) {
            for (var entry : gear.getEnchantments().entrySet()) {
                totalXP += vanillaEnchantPoint * Math.max(1, entry.getValue());
            }
        }

        return Math.max(baseXp, totalXP);
    }

    public boolean canRecycleForXp(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (!item.getEnchantments().isEmpty()) return true;
        if (!plugin.getEnchantmentManager().getEnchantmentsOnItem(item).isEmpty()) return true;
        return item.getType().getMaxDurability() > 0;
    }

    private ItemStack createMysteryDust(CustomEnchantment.EnchantTier tier, int percentage) {
        ItemStack dust = new ItemStack(Material.SUGAR);
        ItemMeta meta = dust.getItemMeta();
        String color = "\u00a7" + tier.getColor();
        meta.setDisplayName(color + "\u00a7lMystery Magic " + tier.getDisplayName() + " Dust " + percentage + "%");
        meta.setLore(Arrays.asList(
                "\u00a77A shimmering " + color + tier.getDisplayName() + "\u00a77 dust",
                "\u00a77imbued with magical energy.",
                "",
                "\u00a7eBonus: " + percentage + "%"
        ));
        meta.getPersistentDataContainer().set(DUST_TIER_KEY, PersistentDataType.STRING, tier.name());
        meta.getPersistentDataContainer().set(DUST_PERCENTAGE_KEY, PersistentDataType.INTEGER, percentage);
        dust.setItemMeta(meta);
        return dust;
    }

    /**
     * Creates a mystery dust token (fire charge) with a random percentage between 1-16%.
     * Used by envoy crate rewards — always gives real dust (no dud), range 1-16%.
     * The player must right-click the fire charge to reveal the actual dust.
     */
    public ItemStack createEnvoyDustToken(CustomEnchantment.EnchantTier tier) {
        int percentage = 1 + random.nextInt(16); // 1–16%, truly random
        return createRandomTierDustToken(tier, percentage, false);
    }

    /**
     * Creates a random dust token for the given tier — identical to what the tinkerer produces.
     * The token must be right-clicked to reveal (may be real dust or a dud).
     */
    public ItemStack createRandomDustToken(CustomEnchantment.EnchantTier tier) {
        int basePercent = plugin.getConfig().getInt("random-dust.base-percent." + tier.name(), 1);
        int cap        = plugin.getConfig().getInt("random-dust.percent-cap", 15);
        int minPct = Math.max(1, basePercent);
        int maxPct = Math.min(cap, basePercent + 4);
        int percentage = minPct + random.nextInt(Math.max(1, maxPct - minPct + 1));
        int dudChance  = Math.max(0, Math.min(100, plugin.getConfig().getInt("random-dust.dud-chance", 30)));
        boolean dud    = random.nextInt(100) < dudChance;
        return createRandomTierDustToken(tier, percentage, dud);
    }

    private ItemStack createRandomTierDustToken(CustomEnchantment.EnchantTier tier, int percentage, boolean dud) {
        ItemStack token = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = token.getItemMeta();
        String color = "\u00a7" + tier.getColor();
        meta.setDisplayName(color + "\u00a7lRandom " + tier.getDisplayName() + " Dust");
        meta.setLore(Arrays.asList(
                "\u00a77Right-click to reveal.",
                "\u00a77Could be real dust... or a dud.",
                "",
                "\u00a78Tier: " + color + tier.getDisplayName()
        ));
        meta.getPersistentDataContainer().set(RANDOM_DUST_TOKEN_KEY, PersistentDataType.STRING, tier.name());
        meta.getPersistentDataContainer().set(DUST_TIER_KEY, PersistentDataType.STRING, tier.name());
        meta.getPersistentDataContainer().set(DUST_PERCENTAGE_KEY, PersistentDataType.INTEGER, percentage);
        meta.getPersistentDataContainer().set(RANDOM_DUST_DUD_KEY, PersistentDataType.INTEGER, dud ? 1 : 0);
        token.setItemMeta(meta);
        return token;
    }

    public boolean isRandomDustToken(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(RANDOM_DUST_TOKEN_KEY, PersistentDataType.STRING);
    }

    public DustOpenResult openRandomDustToken(ItemStack token) {
        if (!isRandomDustToken(token)) return null;
        ItemMeta meta = token.getItemMeta();
        String tierName = meta.getPersistentDataContainer().get(RANDOM_DUST_TOKEN_KEY, PersistentDataType.STRING);
        Integer percentage = meta.getPersistentDataContainer().get(DUST_PERCENTAGE_KEY, PersistentDataType.INTEGER);
        Integer dudFlag = meta.getPersistentDataContainer().get(RANDOM_DUST_DUD_KEY, PersistentDataType.INTEGER);
        if (tierName == null || percentage == null || dudFlag == null) return null;

        CustomEnchantment.EnchantTier tier;
        try {
            tier = CustomEnchantment.EnchantTier.valueOf(tierName);
        } catch (Exception ex) {
            return null;
        }

        boolean dud = dudFlag == 1;
        if (dud) {
            return new DustOpenResult(true, createDudPowder());
        }
        return new DustOpenResult(false, createMysteryDust(tier, percentage));
    }

    private ItemStack createDudPowder() {
        ItemStack dud = new ItemStack(Material.GUNPOWDER);
        ItemMeta meta = dud.getItemMeta();
        meta.setDisplayName("§8§lDud Powder");
        meta.setLore(Arrays.asList(
                "§7This random dust fizzled out.",
                "§7No success bonus from this one."
        ));
        dud.setItemMeta(meta);
        return dud;
    }

    public boolean isMysteryDust(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        if (item.getType() != Material.SUGAR) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(DUST_TIER_KEY, PersistentDataType.STRING)
                && meta.getPersistentDataContainer().has(DUST_PERCENTAGE_KEY, PersistentDataType.INTEGER);
    }

    public CustomEnchantment.EnchantTier getDustTier(ItemStack dust) {
        if (!isMysteryDust(dust)) return null;
        String tierName = dust.getItemMeta().getPersistentDataContainer().get(DUST_TIER_KEY, PersistentDataType.STRING);
        if (tierName == null) return null;
        try {
            return CustomEnchantment.EnchantTier.valueOf(tierName);
        } catch (Exception ex) {
            return null;
        }
    }

    public int getDustPercentage(ItemStack dust) {
        if (!isMysteryDust(dust)) return 0;
        Integer pct = dust.getItemMeta().getPersistentDataContainer().get(DUST_PERCENTAGE_KEY, PersistentDataType.INTEGER);
        return pct == null ? 0 : Math.max(0, Math.min(100, pct));
    }

    public DustApplyResult applyDustToBook(ItemStack dust, ItemStack book) {
        if (!isMysteryDust(dust) || !EnchantBook.isEnchantBook(book) || EnchantBook.isRandomBook(book)) return null;

        CustomEnchantment.EnchantTier dustTier = getDustTier(dust);
        CustomEnchantment.EnchantTier bookTier = EnchantBook.getTier(book);
        if (dustTier == null || bookTier == null || dustTier != bookTier) return null;

        String enchantId = EnchantBook.getEnchantId(book);
        int level = EnchantBook.getEnchantLevel(book);
        int oldSuccess = EnchantBook.getSuccessRate(book);
        int destroy = EnchantBook.getDestroyRate(book);

        CustomEnchantment enchant = plugin.getEnchantmentManager().getEnchantment(enchantId);
        if (enchant == null) return null;

        int boost = getDustPercentage(dust);
        int newSuccess = Math.min(100, oldSuccess + boost);
        int applied = Math.max(0, newSuccess - oldSuccess);

        ItemStack upgradedBook = EnchantBook.createSpecificBook(enchant, level, newSuccess, destroy);
        return new DustApplyResult(upgradedBook, applied, newSuccess);
    }

    public static class DustOpenResult {
        private final boolean dud;
        private final ItemStack reward;

        public DustOpenResult(boolean dud, ItemStack reward) {
            this.dud = dud;
            this.reward = reward;
        }

        public boolean isDud() {
            return dud;
        }

        public ItemStack getReward() {
            return reward;
        }
    }

    public static class DustApplyResult {
        private final ItemStack upgradedBook;
        private final int appliedBoost;
        private final int newSuccessRate;

        public DustApplyResult(ItemStack upgradedBook, int appliedBoost, int newSuccessRate) {
            this.upgradedBook = upgradedBook;
            this.appliedBoost = appliedBoost;
            this.newSuccessRate = newSuccessRate;
        }

        public ItemStack getUpgradedBook() {
            return upgradedBook;
        }

        public int getAppliedBoost() {
            return appliedBoost;
        }

        public int getNewSuccessRate() {
            return newSuccessRate;
        }
    }

}
