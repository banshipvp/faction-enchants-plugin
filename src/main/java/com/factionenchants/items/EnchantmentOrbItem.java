package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for Weapon and Armor Enchantment Orb custom items.
 *
 * Each orb tier allows a player to increase the max enchantment slot count
 * on a piece of gear by 1, up to the orb's defined maximum (e.g. [15] allows
 * up to 15 slots). Applying the orb has a 95% success rate.
 *
 * The expanded slot count is stored in PDC on the gear item itself under
 * {@link #ITEM_MAX_SLOTS_KEY}, and overrides the global
 * {@link com.factionenchants.books.EnchantBook#MAX_ENCHANTS_PER_ITEM} limit
 * when present.
 */
public class EnchantmentOrbItem {

    /** PDC key on weapon orb items. */
    private static final String WEAPON_ORB_KEY    = "weapon_enchant_orb";
    /** PDC key on armor orb items. */
    private static final String ARMOR_ORB_KEY     = "armor_enchant_orb";
    /** PDC key storing the max-slots cap carried by the orb. */
    private static final String ORB_MAX_SLOTS_KEY = "orb_max_slots";

    /** PDC key stored on GEAR items that have had their slots expanded. */
    public static final String ITEM_MAX_SLOTS_KEY = "item_max_enchant_slots";

    // ── Weapon orb range: 10–15 slots ─────────────────────────────────────────
    public static final int WEAPON_ORB_MIN = 10;
    public static final int WEAPON_ORB_MAX = 15;

    // ── Armor orb range: 10–15 slots ──────────────────────────────────────────
    public static final int ARMOR_ORB_MIN  = 10;
    public static final int ARMOR_ORB_MAX  = 15;

    // ── Factory ───────────────────────────────────────────────────────────────

    /**
     * Creates a Weapon Enchantment Orb that allows expanding weapon max slots up to
     * {@code maxSlots}.  Valid range: {@value #WEAPON_ORB_MIN}–{@value #WEAPON_ORB_MAX}.
     */
    public static ItemStack createWeaponOrb(FactionEnchantsPlugin plugin, int maxSlots) {
        maxSlots = Math.max(WEAPON_ORB_MIN, Math.min(WEAPON_ORB_MAX, maxSlots));
        ItemStack orb = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = orb.getItemMeta();
        meta.setDisplayName("§5§lWeapon Enchantment Orb §d[" + maxSlots + "]");
        List<String> lore = new ArrayList<>();
        lore.add("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        lore.add("§e95% Success Rate");
        lore.add("§7");
        lore.add("§6+1 Enchantment Slots");
        lore.add("§6" + maxSlots + " Max Enchantment Slots");
        lore.add("§7");
        lore.add("§fIncreases the # of enchantment");
        lore.add("§fslots on a piece of weapon by 1,");
        lore.add("§fup to a maximum of " + maxSlots + ".");
        lore.add("§fDrag n' Drop onto weapon to apply.");
        lore.addAll(obtainableLore(maxSlots));
        lore.add("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        meta.setLore(lore);
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, WEAPON_ORB_KEY), PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, ORB_MAX_SLOTS_KEY), PersistentDataType.INTEGER, maxSlots);
        orb.setItemMeta(meta);
        return orb;
    }

    /**
     * Creates an Armor Enchantment Orb that allows expanding armor max slots up to
     * {@code maxSlots}.  Valid range: {@value #ARMOR_ORB_MIN}–{@value #ARMOR_ORB_MAX}.
     */
    public static ItemStack createArmorOrb(FactionEnchantsPlugin plugin, int maxSlots) {
        maxSlots = Math.max(ARMOR_ORB_MIN, Math.min(ARMOR_ORB_MAX, maxSlots));
        ItemStack orb = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = orb.getItemMeta();
        meta.setDisplayName("§5§lArmor Enchantment Orb §d[" + maxSlots + "]");
        List<String> lore = new ArrayList<>();
        lore.add("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        lore.add("§e95% Success Rate");
        lore.add("§7");
        lore.add("§6+1 Enchantment Slots");
        lore.add("§6" + maxSlots + " Max Enchantment Slots");
        lore.add("§7");
        lore.add("§fIncreases the # of enchantment");
        lore.add("§fslots on a piece of armor by 1,");
        lore.add("§fup to a maximum of " + maxSlots + ".");
        lore.add("§fDrag n' Drop onto armor to apply.");
        lore.addAll(obtainableLore(maxSlots));
        lore.add("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        meta.setLore(lore);
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, ARMOR_ORB_KEY), PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, ORB_MAX_SLOTS_KEY), PersistentDataType.INTEGER, maxSlots);
        orb.setItemMeta(meta);
        return orb;
    }

    /**
     * Returns the "Obtainable From" lore lines for the given orb tier,
     * or an empty list if that tier has no defined obtain sources.
     */
    private static List<String> obtainableLore(int maxSlots) {
        return switch (maxSlots) {
            case 12 -> List.of(
                    "§7",
                    "§6Obtainable From:",
                    "§8* §f/conquests",
                    "§8* §f/envoys: §7Ultimate",
                    "§8* §f/invasions: §7Aether, Demonic, Dark Space",
                    "§8* §f/minigames",
                    "§8* §f/ruins: §7Guardians",
                    "§8* §f/spacechests: §7Legendary"
            );
            case 13, 14 -> List.of(
                    "§7",
                    "§6Obtainable From:",
                    "§8* §f/bah",
                    "§8* §f/citadel",
                    "§8* §f/conquests",
                    "§8* §f/dungeons: §7Abandoned Spaceship, Destroyed Outpost, Planet Nul, Time Warp",
                    "§8* §f/end: §7Elite Ender Monsters, Timeless Dragon",
                    "§8* §f/envoys: §7Ultimate, Legendary",
                    "§8* §f/invasions: §7Aether, Demonic, Dark Space",
                    "§8* §f/kits: §7Interstellar, Hyperdrive, Lucky §c/vkit",
                    "§8* §f/minigames",
                    "§8* §f/ruins: §7Guardians, Elite Guardians, Elder Elite Guardians, Leviathan",
                    "§8* §f/slot: §7Meta Spin",
                    "§8* §f/spacechests: §7Legendary, Godly",
                    "§8* §f/supernova: §7Black Market",
                    "§8* §f/tournaments: §71st place",
                    "§8* §f/trials: §7Hardcore, Demonic",
                    "§8* §f/xpshop"
            );
            case 15 -> List.of(
                    "§7",
                    "§6Obtainable From:",
                    "§8* §f/conquests",
                    "§8* §f/dungeons: §7Planet Nul, Time Warp",
                    "§8* §f/end: §7Elite Ender Monsters, Timeless Dragon",
                    "§8* §f/kits: §7Hyperdrive",
                    "§8* §f/minigames",
                    "§8* §f/ruins: §7Elite Guardians, Elder Elite Guardians, Leviathan",
                    "§8* §f/slot: §7Meta Spin",
                    "§8* §f/tournaments: §71st place",
                    "§8* §f/trials: §7Demonic"
            );
            default -> List.of(); // [10] and [11] have no defined obtain sources
        };
    }

    // ── Detection ─────────────────────────────────────────────────────────────

    public static boolean isWeaponOrb(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, WEAPON_ORB_KEY), PersistentDataType.BYTE);
    }

    public static boolean isArmorOrb(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, ARMOR_ORB_KEY), PersistentDataType.BYTE);
    }

    /** Returns the max-slots cap stored on the orb, or 0 if unavailable. */
    public static int getOrbMaxSlots(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        Integer val = item.getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey(plugin, ORB_MAX_SLOTS_KEY), PersistentDataType.INTEGER);
        return val != null ? val : 0;
    }

    // ── Per-item max-slot helpers (applied to GEAR) ───────────────────────────

    /**
     * Returns the custom max enchantment slots stored on the gear, or -1 if not set
     * (meaning the global {@link com.factionenchants.books.EnchantBook#MAX_ENCHANTS_PER_ITEM}
     * should be used).
     */
    public static int getItemMaxSlots(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return -1;
        Integer val = item.getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey(plugin, ITEM_MAX_SLOTS_KEY), PersistentDataType.INTEGER);
        return val != null ? val : -1;
    }

    /** Stores the custom max enchantment slots on a gear item in-place. */
    public static void setItemMaxSlots(FactionEnchantsPlugin plugin, ItemStack item, int maxSlots) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer()
                .set(new NamespacedKey(plugin, ITEM_MAX_SLOTS_KEY), PersistentDataType.INTEGER, maxSlots);
        item.setItemMeta(meta);
    }

    // ── Gear-type checks ─────────────────────────────────────────────────────

    /** True if the item is something a Weapon Enchantment Orb can be applied to. */
    public static boolean isWeaponGear(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        String n = item.getType().name();
        return n.endsWith("_SWORD") || n.endsWith("_AXE")
                || item.getType() == Material.BOW
                || item.getType() == Material.CROSSBOW
                || item.getType() == Material.TRIDENT
                || item.getType() == Material.FISHING_ROD;
    }

    /** True if the item is something an Armor Enchantment Orb can be applied to. */
    public static boolean isArmorGear(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        String n = item.getType().name();
        return n.endsWith("_HELMET") || n.endsWith("_CHESTPLATE")
                || n.endsWith("_LEGGINGS") || n.endsWith("_BOOTS")
                || item.getType() == Material.SHIELD
                || item.getType() == Material.ELYTRA;
    }
}
