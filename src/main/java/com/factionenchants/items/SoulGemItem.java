package com.factionenchants.items;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Soul Gem — an emerald that acts as both toggle and fuel for SOUL-tier enchants.
 *
 * Each gem holds a charge count stored in PDC.  Right-clicking toggles it
 * active/inactive.  Soul enchants drain charges on proc (event-based) or once
 * per second (passive, e.g. Divine Immolation).  When charges hit zero the gem
 * auto-deactivates.
 *
 * The lore is rebuilt every time charges or state change via {@link #updateLore}.
 */
public class SoulGemItem {

    // PDC keys
    private static final String KEY_MARKER   = "soul_gem";          // byte 1 = is a soul gem
    private static final String KEY_CHARGES  = "soul_gem_charges";  // int   = remaining charges
    private static final String KEY_ACTIVE   = "soul_gem_active";   // byte  1=on 0=off
    public  static final String KEY_GENERATOR = "soul_gem_generator"; // byte 1 = random generator

    // ── Factory ───────────────────────────────────────────────────────────────

    /** Create a Soul Gem with a specific charge amount. */
    public static ItemStack create(FactionEnchantsPlugin plugin, int charges) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        pdc.set(key(plugin, KEY_MARKER),  PersistentDataType.BYTE,    (byte) 1);
        pdc.set(key(plugin, KEY_CHARGES), PersistentDataType.INTEGER,  charges);
        pdc.set(key(plugin, KEY_ACTIVE),  PersistentDataType.BYTE,    (byte) 0);
        applyMeta(meta, charges, false);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Create the Random Soul Gem Generator — right-click to spawn a gem
     * with a random 100–5000 charge value.
     */
    public static ItemStack createGenerator(FactionEnchantsPlugin plugin) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§d§l✦ Random Soul Gem ✦");
        meta.setLore(List.of(
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Right-click to receive a",
                "§7Soul Gem with a random",
                "§csoul charge §7amount",
                "§8(§c100§8–§c5,000 §8charges)",
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§eRight-click to open"
        ));
        meta.getPersistentDataContainer()
                .set(key(plugin, KEY_GENERATOR), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    // ── Checks ────────────────────────────────────────────────────────────────

    public static boolean isSoulGem(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(key(plugin, KEY_MARKER), PersistentDataType.BYTE);
    }

    public static boolean isGenerator(FactionEnchantsPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(key(plugin, KEY_GENERATOR), PersistentDataType.BYTE);
    }

    public static boolean isActive(FactionEnchantsPlugin plugin, ItemStack item) {
        if (!isSoulGem(plugin, item)) return false;
        Byte val = item.getItemMeta().getPersistentDataContainer()
                .get(key(plugin, KEY_ACTIVE), PersistentDataType.BYTE);
        return val != null && val == 1;
    }

    // ── Charge queries ────────────────────────────────────────────────────────

    public static int getCharges(FactionEnchantsPlugin plugin, ItemStack item) {
        if (!isSoulGem(plugin, item)) return 0;
        Integer val = item.getItemMeta().getPersistentDataContainer()
                .get(key(plugin, KEY_CHARGES), PersistentDataType.INTEGER);
        return val != null ? val : 0;
    }

    // ── Mutations ─────────────────────────────────────────────────────────────

    /** Toggles the active state on the item in-place. Returns the new active state. */
    public static boolean toggle(FactionEnchantsPlugin plugin, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        byte current = pdc.getOrDefault(key(plugin, KEY_ACTIVE), PersistentDataType.BYTE, (byte) 0);
        boolean newActive = current == 0;
        pdc.set(key(plugin, KEY_ACTIVE), PersistentDataType.BYTE, newActive ? (byte) 1 : (byte) 0);
        int charges = pdc.getOrDefault(key(plugin, KEY_CHARGES), PersistentDataType.INTEGER, 0);
        applyMeta(meta, charges, newActive);
        item.setItemMeta(meta);
        return newActive;
    }

    /**
     * Drains {@code amount} charges from the gem item in-place.
     * Returns the remaining charges after drain (may be 0 or negative clamped to 0).
     * Sets active to false if charges reach 0.
     */
    public static int drainCharges(FactionEnchantsPlugin plugin, ItemStack item, int amount) {
        ItemMeta meta = item.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        int current = pdc.getOrDefault(key(plugin, KEY_CHARGES), PersistentDataType.INTEGER, 0);
        int remaining = Math.max(0, current - amount);
        pdc.set(key(plugin, KEY_CHARGES), PersistentDataType.INTEGER, remaining);
        boolean active = remaining > 0 && pdc.getOrDefault(key(plugin, KEY_ACTIVE), PersistentDataType.BYTE, (byte) 0) == 1;
        if (remaining == 0) {
            pdc.set(key(plugin, KEY_ACTIVE), PersistentDataType.BYTE, (byte) 0);
        }
        applyMeta(meta, remaining, active);
        item.setItemMeta(meta);
        return remaining;
    }

    /**
     * Sets charges on the gem to an exact value in-place.
     * Used by split/combine. Auto-deactivates if charges hit 0.
     */
    public static void setCharges(FactionEnchantsPlugin plugin, ItemStack item, int charges) {
        if (!isSoulGem(plugin, item)) return;
        ItemMeta meta = item.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        int clamped = Math.max(0, charges);
        pdc.set(key(plugin, KEY_CHARGES), PersistentDataType.INTEGER, clamped);
        boolean active = clamped > 0
                && pdc.getOrDefault(key(plugin, KEY_ACTIVE), PersistentDataType.BYTE, (byte) 0) == 1;
        if (clamped == 0) pdc.set(key(plugin, KEY_ACTIVE), PersistentDataType.BYTE, (byte) 0);
        applyMeta(meta, clamped, active);
        item.setItemMeta(meta);
    }

    /** Rebuilds the lore from scratch after a charge/state change. */
    public static void updateLore(FactionEnchantsPlugin plugin, ItemStack item) {
        if (!isSoulGem(plugin, item)) return;
        ItemMeta meta = item.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        int charges = pdc.getOrDefault(key(plugin, KEY_CHARGES), PersistentDataType.INTEGER, 0);
        boolean active = pdc.getOrDefault(key(plugin, KEY_ACTIVE), PersistentDataType.BYTE, (byte) 0) == 1;
        applyMeta(meta, charges, active);
        item.setItemMeta(meta);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static void applyMeta(ItemMeta meta, int charges, boolean active) {
        String stateColor = active ? "§a" : "§c";
        String stateText  = active ? "§a§lACTIVE" : "§c§lINACTIVE";
        String fmt = NumberFormat.getNumberInstance(Locale.US).format(charges);

        meta.setDisplayName("§c§l✦ Soul Gem §8[" + stateColor + fmt + " §8souls]");

        List<String> lore = new ArrayList<>();
        lore.add("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        lore.add("§7Status: " + stateText);
        lore.add("§7Charges: §c" + fmt);
        lore.add("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        lore.add("§7Enables §cSOUL §7tier enchants.");
        lore.add("§7Charges drain when enchants");
        lore.add("§7proc or tick passively.");
        lore.add("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        lore.add("§eRight-click to toggle");
        meta.setLore(lore);
    }

    private static NamespacedKey key(FactionEnchantsPlugin plugin, String name) {
        return new NamespacedKey(plugin, name);
    }
}
