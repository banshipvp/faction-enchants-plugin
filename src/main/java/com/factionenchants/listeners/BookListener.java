package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.books.DustManager;
import com.factionenchants.books.EnchantBook;
import com.factionenchants.items.GodlyTransmogScrollItem;
import com.factionenchants.items.HeroicBlackScrollItem;
import com.factionenchants.items.HolyWhiteScrollItem;
import com.factionenchants.items.RandomizationScrollItem;
import com.factionenchants.items.TransmogScrollItem;
import com.factionenchants.items.VanillaBlackScrollItem;
import com.factionenchants.items.WhiteScrollItem;
import com.factionenchants.commands.AlchemistCommand;
import com.factionenchants.commands.EnchanterCommand;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.EnchantmentManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class BookListener implements Listener {

    static final String GODLY_TRANSMOG_GUI_TITLE = "§6Arrange Enchantments";

    private final FactionEnchantsPlugin plugin;
    /** Tracks open Godly Transmog GUI sessions, keyed by player UUID. */
    private final Map<UUID, GodlyTransmogSession> godlyTransmogSessions = new HashMap<>();

    public BookListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    // ── Godly Transmog GUI session ─────────────────────────────────────────────
    private static class GodlyTransmogSession {
        List<String> enchantLines;       // current (possibly reordered) enchant lore lines
        final List<String> otherLoreLines; // non-enchant lore to preserve
        final int playerInvSlot;         // slot in player's regular inventory
        int selectedGuiSlot;             // -1 = nothing selected
        boolean confirmed;

        GodlyTransmogSession(int slot, List<String> enchants, List<String> other) {
            this.playerInvSlot = slot;
            this.enchantLines = new ArrayList<>(enchants);
            this.otherLoreLines = new ArrayList<>(other);
            this.selectedGuiSlot = -1;
            this.confirmed = false;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = event.getView().getTitle();

        // ---- Godly Transmog GUI ----
        if (title.equals(GODLY_TRANSMOG_GUI_TITLE)) {
            event.setCancelled(true);
            GodlyTransmogSession session = godlyTransmogSessions.get(player.getUniqueId());
            if (session == null) return;
            int rawS = event.getRawSlot();
            int topSize = event.getView().getTopInventory().getSize();
            if (rawS < 0 || rawS >= topSize) return; // clicked in player inv area

            if (rawS == 22) { // Confirm
                applyGodlyTransmogOrder(player, session);
                session.confirmed = true;
                godlyTransmogSessions.remove(player.getUniqueId());
                player.closeInventory();
                player.sendMessage("§6Godly Transmog Scroll §aapplied! Enchant order customized.");
                return;
            }
            if (rawS == 18) { // Cancel
                godlyTransmogSessions.remove(player.getUniqueId());
                player.closeInventory();
                player.sendMessage("§cCancelled — no changes applied.");
                return;
            }
            // Enchant slot click
            if (rawS < session.enchantLines.size()) {
                if (session.selectedGuiSlot == -1) {
                    session.selectedGuiSlot = rawS;
                } else if (session.selectedGuiSlot == rawS) {
                    session.selectedGuiSlot = -1;
                } else {
                    // Swap
                    String tmp = session.enchantLines.get(session.selectedGuiSlot);
                    session.enchantLines.set(session.selectedGuiSlot, session.enchantLines.get(rawS));
                    session.enchantLines.set(rawS, tmp);
                    session.selectedGuiSlot = -1;
                }
                refreshGodlyTransmogGUI(event.getView().getTopInventory(), session);
            }
            return;
        }

        // ---- Vanilla Enchanter Sub-GUI ----
        if (title.equals(EnchanterCommand.VANILLA_GUI_TITLE)) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            // Back button
            if (clicked.getType() == Material.SPECTRAL_ARROW) {
                EnchanterCommand.openEnchanterGUI(player, plugin);
                return;
            }

            var vpdc = clicked.getItemMeta().getPersistentDataContainer();
            if (EnchanterCommand.VANILLA_ENCH_KEY != null
                    && vpdc.has(EnchanterCommand.VANILLA_ENCH_KEY, PersistentDataType.STRING)) {
                String enchKey = vpdc.get(EnchanterCommand.VANILLA_ENCH_KEY, PersistentDataType.STRING);
                int level = (EnchanterCommand.VANILLA_ENCH_LEVEL_KEY != null
                        && vpdc.has(EnchanterCommand.VANILLA_ENCH_LEVEL_KEY, PersistentDataType.INTEGER))
                        ? vpdc.get(EnchanterCommand.VANILLA_ENCH_LEVEL_KEY, PersistentDataType.INTEGER) : 1;
                int cost = plugin.getConfig().getInt("enchanter.vanilla." + enchKey + "-cost", 25);
                if (player.getLevel() < cost) {
                    player.sendMessage("\u00a7cYou need \u00a7e" + cost + " XP levels \u00a7cto buy this book!");
                    return;
                }
                org.bukkit.enchantments.Enchantment ench =
                        org.bukkit.enchantments.Enchantment.getByKey(
                                org.bukkit.NamespacedKey.minecraft(enchKey));
                if (ench == null) return;
                player.setLevel(player.getLevel() - cost);
                ItemStack vBook = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta esm = (EnchantmentStorageMeta) vBook.getItemMeta();
                esm.addStoredEnchant(ench, level, true);
                vBook.setItemMeta(esm);
                player.getInventory().addItem(vBook);
                player.sendMessage("\u00a7aYou received a \u00a7f"
                        + EnchanterCommand.prettyKey(enchKey) + " "
                        + EnchanterCommand.toRoman(level) + " \u00a7abook!");
            }
            return;
        }

        // ---- Enchanter GUI ----
        if (title.equals(EnchanterCommand.GUI_TITLE)) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            var pdc = clicked.getItemMeta().getPersistentDataContainer();

            // ── Vanilla enchant book purchase ─────────────────────────────────────────
            if (EnchanterCommand.VANILLA_ENCH_KEY != null
                    && pdc.has(EnchanterCommand.VANILLA_ENCH_KEY, PersistentDataType.STRING)) {
                String enchKey = pdc.get(EnchanterCommand.VANILLA_ENCH_KEY, PersistentDataType.STRING);
                int level = (EnchanterCommand.VANILLA_ENCH_LEVEL_KEY != null
                        && pdc.has(EnchanterCommand.VANILLA_ENCH_LEVEL_KEY, PersistentDataType.INTEGER))
                        ? pdc.get(EnchanterCommand.VANILLA_ENCH_LEVEL_KEY, PersistentDataType.INTEGER) : 1;
                int cost = plugin.getConfig().getInt("enchanter.vanilla." + enchKey + "-cost", 25);
                if (player.getLevel() < cost) {
                    player.sendMessage("\u00a7cYou need \u00a7e" + cost + " XP levels \u00a7cto buy this book!");
                    return;
                }
                org.bukkit.enchantments.Enchantment ench =
                        org.bukkit.enchantments.Enchantment.getByKey(
                                org.bukkit.NamespacedKey.minecraft(enchKey));
                if (ench == null) return;
                player.setLevel(player.getLevel() - cost);
                ItemStack vBook = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta esm = (EnchantmentStorageMeta) vBook.getItemMeta();
                esm.addStoredEnchant(ench, level, true);
                vBook.setItemMeta(esm);
                player.getInventory().addItem(vBook);
                player.sendMessage("\u00a7aYou received a \u00a7f"
                        + EnchanterCommand.prettyKey(enchKey) + " "
                        + EnchanterCommand.toRoman(level) + " \u00a7abook!");
                return;
            }

            // ── Custom tier mystery book purchase ──────────────────────────────
            if (!pdc.has(EnchantBook.BOOK_TIER_KEY, PersistentDataType.STRING)) return;
            String tierStr = pdc.get(EnchantBook.BOOK_TIER_KEY, PersistentDataType.STRING);
            CustomEnchantment.EnchantTier tier;
            try { tier = CustomEnchantment.EnchantTier.valueOf(tierStr); }
            catch (Exception e) { return; }
            int cost = plugin.getConfig().getInt("enchanter." + tier.name().toLowerCase() + "-cost", 5);
            if (player.getLevel() < cost) {
                player.sendMessage("\u00a7cYou need \u00a7e" + cost + " XP levels \u00a7cto buy this book!");
                return;
            }
            player.setLevel(player.getLevel() - cost);
            ItemStack book = EnchantBook.createRandomBook(tier);
            player.getInventory().addItem(book);
            String color = "\u00a7" + tier.getColor();
            player.sendMessage("\u00a7aYou received a " + color + tier.getDisplayName() + " \u00a7amystery book!");
            return;
        }

        // ---- Enchants Browser GUI ----
        if (title.startsWith(com.factionenchants.commands.EnchantsCommand.GUI_TITLE)) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            // Handle close button
            if (clicked.getType() == Material.BARRIER) {
                player.closeInventory();
                return;
            }

            var pdc = clicked.getItemMeta().getPersistentDataContainer();
            if (!pdc.has(EnchantBook.BOOK_TIER_KEY, PersistentDataType.STRING)) return;
            String tierStr = pdc.get(EnchantBook.BOOK_TIER_KEY, PersistentDataType.STRING);
            CustomEnchantment.EnchantTier tier;
            try { tier = CustomEnchantment.EnchantTier.valueOf(tierStr); }
            catch (Exception e) { return; }

            // Open the new tier's page
            new com.factionenchants.commands.EnchantsCommand(plugin).openEnchantsGUI(player, tier);
            return;
        }

        // ---- Alchemist GUI ----
        if (title.equals(AlchemistCommand.GUI_TITLE)) {
            Inventory topInventory = event.getView().getTopInventory();
            int topSize = event.getView().getTopInventory().getSize();
            int rawSlot = event.getRawSlot();
            boolean inTop = rawSlot >= 0 && rawSlot < topSize;

            if (!inTop) {
                if (event.isShiftClick()) {
                    if (moveOneBookFromClickedSlotToAlchemist(event)) {
                        event.setCancelled(true);
                        scheduleAlchemistPreview(topInventory);
                    }
                } else if (event.isLeftClick()) {
                    if (moveOneBookFromClickedSlotToAlchemist(event)) {
                        event.setCancelled(true);
                        scheduleAlchemistPreview(topInventory);
                    }
                }
                return;
            }

            if (rawSlot == 11 || rawSlot == 15) {
                scheduleAlchemistPreview(topInventory);
                return;
            }

            event.setCancelled(true);
            if (rawSlot == 13) {
                ItemStack book1 = event.getInventory().getItem(11);
                ItemStack book2 = event.getInventory().getItem(15);
                if (!EnchantBook.isEnchantBook(book1) || !EnchantBook.isEnchantBook(book2)) {
                    player.sendMessage("\u00a7cPlace two enchant books in the slots first!");
                    return;
                }

                int cost = plugin.getBookManager().getCombineCost(book1, book2);
                if (cost < 1) {
                    player.sendMessage("\u00a7cThese books cannot be combined.");
                    return;
                }

                boolean success = plugin.getBookManager().combineBooks(player, book1, book2);
                if (success) {
                    event.getInventory().setItem(11, null);
                    event.getInventory().setItem(15, null);
                    player.sendMessage("\u00a7aBooks successfully combined for \u00a7e" + cost + " XP levels\u00a7a!");
                    updateAlchemistPreview(topInventory);
                } else {
                    player.sendMessage("\u00a7cFailed! Books must be same enchant, valid levels, below max, and result cannot exceed max level.");
                }
            }
            return;
        }

        // ---- Apply enchant: drag/click enchant book onto gear ----
        ItemStack cursor = event.getCursor();
        ItemStack current = event.getCurrentItem();
        if (cursor == null || cursor.getType().isAir()) return;
        if (current == null || current.getType().isAir()) return;

        // ---- Apply White Scroll: hold scroll on cursor, click gear ----
        if (WhiteScrollItem.isWhiteScroll(plugin, cursor)) {
            event.setCancelled(true);
            if (!isApplicableGear(current)) {
                player.sendMessage("§cYou can only apply a White Scroll to gear!");
                return;
            }
            if (WhiteScrollItem.isProtected(plugin, current)) {
                player.sendMessage("§cThat item is already §fPROTECTED§c!");
                return;
            }
            ItemStack updatedGear = current.clone();
            WhiteScrollItem.applyProtection(plugin, updatedGear);
            if (event.getClickedInventory() != null) {
                event.getClickedInventory().setItem(event.getSlot(), updatedGear);
            } else {
                event.getView().setItem(event.getRawSlot(), updatedGear);
            }
            if (cursor.getAmount() > 1) {
                cursor.setAmount(cursor.getAmount() - 1);
                event.getView().setCursor(cursor);
            } else {
                event.getView().setCursor(null);
            }
            player.sendMessage("§f❆ §aYour item is now §fPROTECTED §aby a White Scroll!");
            player.updateInventory();
            return;
        }

        // ---- Apply Holy White Scroll: gear must have White Scroll protection ----
        if (HolyWhiteScrollItem.isHolyWhiteScroll(plugin, cursor)) {
            event.setCancelled(true);
            if (!isApplicableGear(current)) {
                player.sendMessage("§cYou can only apply a Holy White Scroll to gear!");
                return;
            }
            if (!WhiteScrollItem.isProtected(plugin, current)) {
                player.sendMessage("§cThis item must have a §fWhite Scroll §capplied first!");
                return;
            }
            if (HolyWhiteScrollItem.isBlessed(plugin, current)) {
                player.sendMessage("§cThat item is already §dBLESSED§c!");
                return;
            }
            ItemStack blessedGear = current.clone();
            WhiteScrollItem.removeProtection(plugin, blessedGear);
            HolyWhiteScrollItem.applyBlessing(plugin, blessedGear);
            setInvItem(event, blessedGear);
            consumeCursorOne(event, cursor);
            player.sendMessage("§d✦ §aYour item is now §dBLESSED§a! It will be kept on death.");
            player.updateInventory();
            return;
        }

        // ---- Heroic Black Scroll: extracts any enchant (incl. Heroic), 10-25% success book ----
        if (HeroicBlackScrollItem.isHeroicBlackScroll(plugin, cursor)) {
            event.setCancelled(true);
            Map<CustomEnchantment, Integer> hEnchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(current);
            if (hEnchants.isEmpty()) {
                player.sendMessage("§cThat item has no custom enchants to extract.");
                return;
            }
            List<Map.Entry<CustomEnchantment, Integer>> hEntries = new ArrayList<>(hEnchants.entrySet());
            Map.Entry<CustomEnchantment, Integer> hExtracted = hEntries.get(ThreadLocalRandom.current().nextInt(hEntries.size()));
            int hSuccess = 10 + ThreadLocalRandom.current().nextInt(16); // 10–25
            ItemStack hUpdated = current.clone();
            plugin.getEnchantmentManager().removeEnchantment(hUpdated, hExtracted.getKey());
            setInvItem(event, hUpdated);
            ItemStack hBook = EnchantBook.createSpecificBook(hExtracted.getKey(), hExtracted.getValue(), hSuccess, 0);
            player.getInventory().addItem(hBook).values().forEach(l -> player.getWorld().dropItemNaturally(player.getLocation(), l));
            consumeCursorOne(event, cursor);
            player.updateInventory();
            player.sendMessage("§dHeroic Black Scroll §aextracted §e" + hExtracted.getKey().getDisplayName()
                    + " " + EnchantmentManager.toRoman(hExtracted.getValue()) + "§a as a §e" + hSuccess + "% §asuccess book.");
            return;
        }

        // ---- Vanilla Black Scroll: extracts a vanilla enchant, 95% success book ----
        if (VanillaBlackScrollItem.isVanillaBlackScroll(plugin, cursor)) {
            event.setCancelled(true);
            Map<org.bukkit.enchantments.Enchantment, Integer> vEnchants = current.getEnchantments();
            List<Map.Entry<org.bukkit.enchantments.Enchantment, Integer>> vObtainable = vEnchants.entrySet().stream()
                    .filter(e -> isObtainableVanillaEnchant(e.getKey()))
                    .collect(Collectors.toList());
            if (vObtainable.isEmpty()) {
                player.sendMessage("§cThat item has no obtainable vanilla enchants to extract.");
                return;
            }
            Map.Entry<org.bukkit.enchantments.Enchantment, Integer> vExtracted =
                    vObtainable.get(ThreadLocalRandom.current().nextInt(vObtainable.size()));
            ItemStack vUpdated = current.clone();
            vUpdated.removeEnchantment(vExtracted.getKey());
            setInvItem(event, vUpdated);
            ItemStack vBook = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta vEsm = (EnchantmentStorageMeta) vBook.getItemMeta();
            vEsm.addStoredEnchant(vExtracted.getKey(), vExtracted.getValue(), true);
            vBook.setItemMeta(vEsm);
            player.getInventory().addItem(vBook).values().forEach(l -> player.getWorld().dropItemNaturally(player.getLocation(), l));
            consumeCursorOne(event, cursor);
            player.updateInventory();
            player.sendMessage("§7Vanilla Black Scroll §aextracted §f"
                    + prettyEnchantName(vExtracted.getKey().getKey().getKey())
                    + " " + EnchantmentManager.toRoman(vExtracted.getValue()) + "§a.");
            return;
        }

        // ---- Transmog Scroll: sort enchants by rarity, add count to name ----
        if (TransmogScrollItem.isTransmogScroll(plugin, cursor)) {
            event.setCancelled(true);
            if (!isApplicableGear(current)) {
                player.sendMessage("§cYou can only apply a Transmog Scroll to gear!");
                return;
            }
            ItemStack transmogResult = applyTransmog(current.clone());
            setInvItem(event, transmogResult);
            consumeCursorOne(event, cursor);
            player.sendMessage("§eTransmog Scroll §aapplied! Enchants sorted by rarity.");
            player.updateInventory();
            return;
        }

        // ---- Godly Transmog Scroll: open reorder GUI ----
        if (GodlyTransmogScrollItem.isGodlyTransmogScroll(plugin, cursor)) {
            event.setCancelled(true);
            if (!isApplicableGear(current)) {
                player.sendMessage("§cYou can only apply a Godly Transmog Scroll to gear!");
                return;
            }
            Map<CustomEnchantment, Integer> gEnchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(current);
            if (gEnchants.isEmpty()) {
                player.sendMessage("§cThat item has no custom enchants to arrange.");
                return;
            }
            // Determine the player-inventory slot of the clicked item
            int clickedSlot = event.getSlot();
            openGodlyTransmogGUI(player, current, clickedSlot);
            consumeCursorOne(event, cursor);
            player.updateInventory();
            return;
        }

        // ---- Randomization Scroll: reroll rates on a specific-tier enchant book ----
        if (RandomizationScrollItem.isRandomizationScroll(plugin, cursor)) {
            event.setCancelled(true);
            if (!EnchantBook.isEnchantBook(current) || EnchantBook.isRandomBook(current)) {
                player.sendMessage("§cApply a Randomization Scroll to a revealed enchant book!");
                return;
            }
            String rTarget = RandomizationScrollItem.getTargetTier(plugin, cursor);
            CustomEnchantment.EnchantTier rBookTier = EnchantBook.getTier(current);
            if (!"ANY".equals(rTarget) && (rBookTier == null || !rBookTier.name().equals(rTarget))) {
                String tc = switch (rTarget != null ? rTarget : "") {
                    case "ULTIMATE" -> "§e"; case "LEGENDARY" -> "§6"; default -> "§f";
                };
                player.sendMessage("§cThis scroll can only be applied to " + tc
                        + (rTarget != null ? rTarget : "a matching") + " §cenchant books!");
                return;
            }
            int rSuccess = rerollSuccessRate(rBookTier);
            int rDestroy = rerollDestroyRate(rBookTier);
            ItemStack rBook = rebuildBookRates(current, rSuccess, rDestroy);
            setInvItem(event, rBook);
            consumeCursorOne(event, cursor);
            player.sendMessage("§aRates rerolled! §eSuccess: " + rSuccess + "% §a| §cDestroy: " + rDestroy + "%");
            player.updateInventory();
            return;
        }

        if (isBlackScroll(cursor)) {
            event.setCancelled(true);

            Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(current);
            // Regular Black Scroll cannot extract Heroic-tier enchants
            List<Map.Entry<CustomEnchantment, Integer>> entries = enchants.entrySet().stream()
                    .filter(e -> e.getKey().getTier() != CustomEnchantment.EnchantTier.HEROIC)
                    .collect(Collectors.toList());
            if (entries.isEmpty()) {
                player.sendMessage("§cThat item has no extractable custom enchants. Use a §dHeroic Black Scroll §cfor Heroic enchants.");
                return;
            }

            Map.Entry<CustomEnchantment, Integer> extracted = entries.get(ThreadLocalRandom.current().nextInt(entries.size()));

            ItemStack updatedTarget = current.clone();
            plugin.getEnchantmentManager().removeEnchantment(updatedTarget, extracted.getKey());
            setInvItem(event, updatedTarget);

            // Always produce a 95% success book
            ItemStack extractedBook = EnchantBook.createSpecificBook(extracted.getKey(), extracted.getValue(), 95, 0);
            player.getInventory().addItem(extractedBook).values().forEach(leftover ->
                    player.getWorld().dropItemNaturally(player.getLocation(), leftover));

            consumeCursorOne(event, cursor);
            player.updateInventory();
            player.sendMessage("§aBlack Scroll extracted §e" + extracted.getKey().getDisplayName() + " " + EnchantmentManager.toRoman(extracted.getValue()) + "§a as a §e95% §asuccess book.");
            return;
        }

        DustManager dustManager = plugin.getDustManager();
        if (dustManager.isMysteryDust(cursor)) {
            event.setCancelled(true);
            ItemStack savedDust = cursor.clone();

            if (!EnchantBook.isEnchantBook(current) || EnchantBook.isRandomBook(current)) {
                event.getView().setCursor(savedDust);
                player.updateInventory();
                player.sendMessage("§cYou can only apply dust to a revealed enchant book.");
                return;
            }

            CustomEnchantment.EnchantTier dustTier = dustManager.getDustTier(cursor);
            CustomEnchantment.EnchantTier bookTier = EnchantBook.getTier(current);
            if (dustTier == null || bookTier == null || dustTier != bookTier) {
                event.getView().setCursor(savedDust);
                player.updateInventory();
                player.sendMessage("§cDust tier must match the enchant book tier.");
                return;
            }

            DustManager.DustApplyResult result = dustManager.applyDustToBook(cursor, current);
            if (result == null) {
                event.getView().setCursor(savedDust);
                player.updateInventory();
                player.sendMessage("§cFailed to apply dust to this book.");
                return;
            }

            if (event.getClickedInventory() != null) {
                event.getClickedInventory().setItem(event.getSlot(), result.getUpgradedBook());
            } else {
                event.getView().setItem(event.getRawSlot(), result.getUpgradedBook());
            }

            if (cursor.getAmount() > 1) {
                cursor.setAmount(cursor.getAmount() - 1);
                event.getView().setCursor(cursor);
            } else {
                event.getView().setCursor(null);
            }

            player.updateInventory();
            player.sendMessage("§aDust applied! §e+" + result.getAppliedBoost() + "% §asuccess (now §e" + result.getNewSuccessRate() + "%§a).");
            return;
        }

        // ---- Apply vanilla enchanted book onto gear (drag/click) ----
        if (cursor.getType() == Material.ENCHANTED_BOOK
                && !EnchantBook.isEnchantBook(cursor)
                && cursor.getItemMeta() instanceof EnchantmentStorageMeta storageMeta
                && !storageMeta.getStoredEnchants().isEmpty()
                && isGear(current)) {

            event.setCancelled(true);

            // Check each stored enchantment can apply to the target
            Map<org.bukkit.enchantments.Enchantment, Integer> stored = storageMeta.getStoredEnchants();
            boolean anyApplicable = stored.keySet().stream()
                    .anyMatch(e -> e.canEnchantItem(current));
            if (!anyApplicable) {
                player.sendMessage("\u00a7cNone of the enchantments in that book can be applied to this item.");
                return;
            }

            ItemStack result = current.clone();
            List<String> applied = new ArrayList<>();
            List<String> skipped = new ArrayList<>();

            for (var entry : stored.entrySet()) {
                org.bukkit.enchantments.Enchantment ench = entry.getKey();
                int lvl = entry.getValue();
                if (!ench.canEnchantItem(result)) {
                    skipped.add(ench.getKey().getKey());
                    continue;
                }
                // Combine levels if same enchant already present
                int existing = result.getEnchantmentLevel(ench);
                int newLvl = (existing == lvl) ? Math.min(lvl + 1, ench.getMaxLevel()) : Math.max(existing, lvl);
                result.addUnsafeEnchantment(ench, newLvl);
                applied.add(prettyEnchantName(ench.getKey().getKey()) + " " + EnchantmentManager.toRoman(newLvl));
            }

            if (applied.isEmpty()) {
                player.sendMessage("\u00a7cNo enchantments from that book could be applied to this item.");
                return;
            }

            if (event.getClickedInventory() != null) {
                event.getClickedInventory().setItem(event.getSlot(), result);
            } else {
                event.getView().setItem(event.getRawSlot(), result);
            }

            if (cursor.getAmount() > 1) {
                cursor.setAmount(cursor.getAmount() - 1);
                event.getView().setCursor(cursor);
            } else {
                event.getView().setCursor(null);
            }
            player.updateInventory();
            player.sendMessage("\u00a7aApplied: \u00a7f" + String.join("\u00a77, \u00a7f", applied));
            if (!skipped.isEmpty()) {
                player.sendMessage("\u00a7eSkipped (incompatible): \u00a77" + String.join(", ", skipped));
            }
            return;
        }

        if (!EnchantBook.isEnchantBook(cursor) || EnchantBook.isRandomBook(cursor)) return;

        // cursor is a specific enchant book, current is the target gear
        String enchantId = EnchantBook.getEnchantId(cursor);
        int level = EnchantBook.getEnchantLevel(cursor);
        CustomEnchantment enchant = plugin.getEnchantmentManager().getEnchantment(enchantId);
        if (enchant == null) return;
        ItemStack savedBook = cursor.clone();
        if (!enchant.canApplyTo(current)) {
            event.setCancelled(true);
            event.getView().setCursor(savedBook);
            player.updateInventory();
            player.sendMessage("\u00a7cThis enchant cannot be applied to that item!");
            return;
        }

        Map<CustomEnchantment, Integer> existing = plugin.getEnchantmentManager().getEnchantmentsOnItem(current);

        // Check Heroic prerequisites
        String prereq = enchant.getPrerequisiteEnchantId();
        if (prereq != null) {
            boolean hasPrereq = existing.keySet().stream().anyMatch(e -> e.getId().equals(prereq));
            if (!hasPrereq) {
                event.setCancelled(true);
                event.getView().setCursor(savedBook);
                player.updateInventory();
                player.sendMessage("\u00a7cThis enchant requires \u00a7e" + prereq + " \u00a7cto be applied first!");
                return;
            }
        }
        if (!existing.containsKey(enchant) && existing.size() >= EnchantBook.MAX_ENCHANTS_PER_ITEM) {
            event.setCancelled(true);
            event.getView().setCursor(savedBook);
            player.updateInventory();
            player.sendMessage("\u00a7cThis item already has the maximum of \u00a7e" + EnchantBook.MAX_ENCHANTS_PER_ITEM + " \u00a7cenchants!");
            return;
        }
        if (existing.containsKey(enchant)) {
            int existingLevel = existing.get(enchant);
            if (existingLevel >= level) {
                String color = "\u00a7" + enchant.getTier().getColor();
                event.setCancelled(true);
                event.getView().setCursor(savedBook);
                player.updateInventory();
                player.sendMessage("\u00a7cYou already have " + color + enchant.getDisplayName() + " "
                        + EnchantmentManager.toRoman(existingLevel) + " \u00a7cor higher on this item!");
                return;
            }
        }

        event.setCancelled(true);
        int baseSuccessRate = Math.max(0, Math.min(100, EnchantBook.getSuccessRate(cursor)));
        int destroyRate = Math.max(0, Math.min(100, EnchantBook.getDestroyRate(cursor)));

        // Apply faction success rate upgrade bonus
        int factionBonus = 0;
        try {
            org.bukkit.plugin.Plugin sfPlugin = org.bukkit.Bukkit.getPluginManager().getPlugin("SimpleFactions");
            if (sfPlugin instanceof local.simplefactions.SimpleFactionsPlugin sfp) {
                local.simplefactions.FactionManager.Faction playerFaction =
                        sfp.getFactionManager().getFaction(player.getUniqueId());
                if (playerFaction != null) factionBonus = playerFaction.getSuccessRateBonus();
            }
        } catch (Exception ignored) {}

        int successRate = Math.min(100, baseSuccessRate + factionBonus);
        boolean success = ThreadLocalRandom.current().nextInt(100) < successRate;

        if (success) {
            ItemStack enchanted = plugin.getEnchantmentManager().applyEnchantment(current.clone(), enchant, level);
            if (event.getClickedInventory() != null) {
                event.getClickedInventory().setItem(event.getSlot(), enchanted);
            } else {
                event.getView().setItem(event.getRawSlot(), enchanted);
            }
            player.updateInventory();
            String color = "\u00a7" + enchant.getTier().getColor();
            player.sendMessage("\u00a7aApplied " + color + enchant.getDisplayName() + " " + EnchantmentManager.toRoman(level) + " \u00a7ato your item!");            callSFChallenge(player, local.simplefactions.ChallengeManager.TrackerType.ENCHANT_APPLY);        } else {
            boolean destroy = ThreadLocalRandom.current().nextInt(100) < destroyRate;
            if (destroy) {
                // Check if gear has whitescroll protection — absorb the hit instead
                if (WhiteScrollItem.isProtected(plugin, current)) {
                    ItemStack saved = current.clone();
                    WhiteScrollItem.removeProtection(plugin, saved);
                    if (event.getClickedInventory() != null) {
                        event.getClickedInventory().setItem(event.getSlot(), saved);
                    } else {
                        event.getView().setItem(event.getRawSlot(), saved);
                    }
                    player.sendMessage("§f✦ §cYour §fWhite Scroll §cabsorbed the destruction! Protection consumed.");
                } else {
                if (event.getClickedInventory() != null) {
                    event.getClickedInventory().setItem(event.getSlot(), null);
                } else {
                    event.getView().setItem(event.getRawSlot(), null);
                }
                player.sendMessage("\u00a7cEnchant failed and your gear was destroyed!");
                }
            } else {
                // Explicitly re-set the item to prevent client-server desync / gear duplication
                if (event.getClickedInventory() != null) {
                    event.getClickedInventory().setItem(event.getSlot(), current);
                } else {
                    event.getView().setItem(event.getRawSlot(), current);
                }
                player.sendMessage("\u00a7cEnchant failed! Your gear survived.");
            }
        } // end else (destroy block)

        // Consume one book from cursor
        if (cursor.getAmount() > 1) {
            cursor.setAmount(cursor.getAmount() - 1);
            event.getView().setCursor(cursor);
        } else {
            event.getView().setCursor(null);
        }
        player.updateInventory();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = event.getItem();
        if (item == null || !EnchantBook.isEnchantBook(item)) return;
        // Only handle mystery books here — specific books are applied via inventory click
        if (!EnchantBook.isRandomBook(item)) return;

        Player player = event.getPlayer();
        event.setCancelled(true);

        CustomEnchantment.EnchantTier tier = EnchantBook.getTier(item);
        if (tier == null) return;
        ItemStack revealed = plugin.getBookManager().openRandomBook(player, tier);
        if (item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
        else player.getInventory().setItemInMainHand(null);
        if (revealed != null) {
            player.getInventory().addItem(revealed);
            player.sendMessage("\u00a7aYou revealed your enchant book!");
            callSFChallenge(player, local.simplefactions.ChallengeManager.TrackerType.BOOK_OPEN);
        }
    }

    // ── SimpleFactions challenge hook ─────────────────────────────────────────

    private void callSFChallenge(org.bukkit.entity.Player player,
                                  local.simplefactions.ChallengeManager.TrackerType type) {
        org.bukkit.plugin.Plugin sfPlugin = org.bukkit.Bukkit.getPluginManager().getPlugin("SimpleFactions");
        if (!(sfPlugin instanceof local.simplefactions.SimpleFactionsPlugin sfp)) return;
        local.simplefactions.ChallengeManager cm = sfp.getChallengeManager();
        if (cm != null) cm.increment(player.getUniqueId(), player.getName(), type);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getView().getTitle().equals(AlchemistCommand.GUI_TITLE)) return;
        Inventory topInventory = event.getView().getTopInventory();
        int topSize = topInventory.getSize();

        boolean affectsTop = event.getRawSlots().stream().anyMatch(slot -> slot < topSize);
        if (!affectsTop) return;

        boolean valid = event.getRawSlots().stream()
                .filter(slot -> slot < topSize)
                .allMatch(slot -> slot == 11 || slot == 15);
        if (!valid) {
            event.setCancelled(true);
            return;
        }

        scheduleAlchemistPreview(topInventory);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        // Clean up godly transmog session if closed without confirming
        if (event.getView().getTitle().equals(GODLY_TRANSMOG_GUI_TITLE)) {
            godlyTransmogSessions.remove(player.getUniqueId());
            return;
        }

        if (!event.getView().getTitle().equals(AlchemistCommand.GUI_TITLE)) return;

        Inventory top = event.getView().getTopInventory();
        returnItemToPlayer(player, top.getItem(11));
        returnItemToPlayer(player, top.getItem(15));
        top.setItem(11, null);
        top.setItem(15, null);
    }

    private boolean moveBookToAlchemistSlot(org.bukkit.inventory.Inventory topInventory, ItemStack shiftItem) {
        int targetSlot = topInventory.getItem(11) == null ? 11 : (topInventory.getItem(15) == null ? 15 : -1);
        if (targetSlot == -1) return false;

        ItemStack clone = shiftItem.clone();
        clone.setAmount(1);
        topInventory.setItem(targetSlot, clone);
        return true;
    }

    private boolean moveOneBookFromClickedSlotToAlchemist(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return false;
        ItemStack clicked = event.getClickedInventory().getItem(event.getSlot());
        if (!EnchantBook.isEnchantBook(clicked)) return false;

        var topInventory = event.getView().getTopInventory();
        if (!moveBookToAlchemistSlot(topInventory, clicked.clone())) return false;

        int newAmount = clicked.getAmount() - 1;
        if (newAmount <= 0) {
            event.getClickedInventory().setItem(event.getSlot(), null);
        } else {
            clicked.setAmount(newAmount);
            event.getClickedInventory().setItem(event.getSlot(), clicked);
        }

        if (event.getWhoClicked() instanceof Player player) {
            player.updateInventory();
        }
        return true;
    }

    private void scheduleAlchemistPreview(Inventory topInventory) {
        Bukkit.getScheduler().runTask(plugin, () -> updateAlchemistPreview(topInventory));
    }

    private void updateAlchemistPreview(Inventory topInventory) {
        ItemStack book1 = topInventory.getItem(11);
        ItemStack book2 = topInventory.getItem(15);

        ItemStack preview = plugin.getBookManager().getCombinePreview(book1, book2);
        if (preview == null) {
            ItemStack placeholder = new ItemStack(Material.BOOK);
            ItemMeta meta = placeholder.getItemMeta();
            meta.setDisplayName("§b§lCombine Preview");
            meta.setLore(Arrays.asList(
                    "§7Place two compatible books",
                    "§7to preview the result."
            ));
            placeholder.setItemMeta(meta);
            topInventory.setItem(22, placeholder);
            return;
        }

        int cost = plugin.getBookManager().getCombineCost(book1, book2);
        ItemMeta meta = preview.getItemMeta();
        List<String> lore = meta.hasLore() ? new java.util.ArrayList<>(meta.getLore()) : new java.util.ArrayList<>();
        lore.add("");
        lore.add("§eCombine Cost: §f" + Math.max(1, cost) + " XP levels");
        lore.add("§7Click §aCombine Books §7to craft this.");
        meta.setLore(lore);
        preview.setItemMeta(meta);
        topInventory.setItem(22, preview);
    }

    private void returnItemToPlayer(Player player, ItemStack item) {
        if (item == null || item.getType().isAir()) return;
        var leftovers = player.getInventory().addItem(item);
        leftovers.values().forEach(left -> player.getWorld().dropItemNaturally(player.getLocation(), left));
    }

    /**
     * Gear items that can have White Scroll protection applied
     * (armor, weapons, tools — excludes books).
     */
    private boolean isApplicableGear(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        String name = item.getType().name();
        if (name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS")) return true;
        if (name.endsWith("_SWORD") || name.endsWith("_AXE")) return true;
        if (name.endsWith("_PICKAXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE")) return true;
        Material m = item.getType();
        return m == Material.BOW || m == Material.CROSSBOW || m == Material.TRIDENT
                || m == Material.FISHING_ROD || m == Material.SHIELD || m == Material.ELYTRA;
    }

    /** True for any wearable/wieldable item that can receive enchantments. */
    private boolean isGear(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        String n = item.getType().name();
        return n.endsWith("_SWORD") || n.endsWith("_AXE") || n.endsWith("_PICKAXE")
                || n.endsWith("_SHOVEL") || n.endsWith("_HOE")
                || n.endsWith("_HELMET") || n.endsWith("_CHESTPLATE")
                || n.endsWith("_LEGGINGS") || n.endsWith("_BOOTS")
                || item.getType() == Material.BOW
                || item.getType() == Material.CROSSBOW
                || item.getType() == Material.TRIDENT
                || item.getType() == Material.FISHING_ROD
                || item.getType() == Material.SHIELD
                || item.getType() == Material.ELYTRA
                || item.getType() == Material.BOOK
                || item.getType() == Material.ENCHANTED_BOOK;
    }

    /** Converts a snake_case enchantment key to a display-friendly Title Case string. */
    private String prettyEnchantName(String key) {
        String[] parts = key.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (!p.isEmpty()) {
                if (!sb.isEmpty()) sb.append(' ');
                sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /** True only for the regular Black Scroll (PDC-based, never matches Heroic/Vanilla variants). */
    private boolean isBlackScroll(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, "black_scroll"), PersistentDataType.BYTE);
    }

    // ── Shared cursor/item helpers ─────────────────────────────────────────────

    /** Sets the item at the clicked slot, whether in a top or bottom inventory. */
    private void setInvItem(InventoryClickEvent event, ItemStack newItem) {
        if (event.getClickedInventory() != null) {
            event.getClickedInventory().setItem(event.getSlot(), newItem);
        } else {
            event.getView().setItem(event.getRawSlot(), newItem);
        }
    }

    /** Consumes one unit from the cursor item. */
    private void consumeCursorOne(InventoryClickEvent event, ItemStack cursor) {
        if (cursor.getAmount() > 1) {
            cursor.setAmount(cursor.getAmount() - 1);
            event.getView().setCursor(cursor);
        } else {
            event.getView().setCursor(null);
        }
    }

    /** Returns true if a vanilla enchantment is obtainable (not commands-only). */
    private boolean isObtainableVanillaEnchant(org.bukkit.enchantments.Enchantment e) {
        String key = e.getKey().getKey();
        // All standard obtainable vanilla enchantments:
        return switch (key) {
            case "sharpness", "smite", "bane_of_arthropods", "knockback", "fire_aspect",
                 "looting", "sweeping", "efficiency", "silk_touch", "unbreaking", "fortune",
                 "power", "punch", "flame", "infinity", "luck_of_the_sea", "lure",
                 "loyalty", "impaling", "riptide", "channeling", "multishot", "quick_charge",
                 "piercing", "protection", "fire_protection", "blast_protection",
                 "projectile_protection", "feather_falling", "respiration", "aqua_affinity",
                 "thorns", "depth_strider", "frost_walker", "mending", "binding_curse",
                 "vanishing_curse", "soul_speed", "swift_sneak" -> true;
            default -> false;
        };
    }

    // ── Transmog helpers ───────────────────────────────────────────────────────

    private boolean isCustomEnchantLine(String line) {
        String stripped = line.replaceAll("§[0-9a-fk-or]", "").trim();
        for (CustomEnchantment enchant : plugin.getEnchantmentManager().getAllEnchantments()) {
            String name = enchant.getDisplayName().replaceAll("§[0-9a-fk-or]", "").trim();
            for (int lvl = 1; lvl <= enchant.getMaxLevel(); lvl++) {
                if (stripped.equals(name + " " + EnchantmentManager.toRoman(lvl))
                        || stripped.equals(name + " " + lvl)) return true;
            }
        }
        return false;
    }

    private int enchantTierOrder(String loreLine) {
        String stripped = loreLine.replaceAll("§[0-9a-fk-or]", "").trim();
        for (CustomEnchantment enchant : plugin.getEnchantmentManager().getAllEnchantments()) {
            String name = enchant.getDisplayName().replaceAll("§[0-9a-fk-or]", "").trim();
            for (int lvl = 1; lvl <= enchant.getMaxLevel(); lvl++) {
                if (stripped.equals(name + " " + EnchantmentManager.toRoman(lvl))
                        || stripped.equals(name + " " + lvl)) return enchant.getTier().ordinal();
            }
        }
        return Integer.MAX_VALUE;
    }

    /** Sorts custom enchants by rarity (Simple→Mastery) and appends enchant count to item name. */
    private ItemStack applyTransmog(ItemStack gear) {
        ItemMeta meta = gear.getItemMeta();
        if (meta == null) return gear;
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        List<String> enchantLines = new ArrayList<>();
        List<String> otherLines = new ArrayList<>();
        for (String line : lore) {
            if (isCustomEnchantLine(line)) enchantLines.add(line);
            else otherLines.add(line);
        }
        enchantLines.sort((a, b) -> Integer.compare(enchantTierOrder(a), enchantTierOrder(b)));
        List<String> newLore = new ArrayList<>(otherLines);
        newLore.addAll(enchantLines);
        meta.setLore(newLore);
        // Append enchant count to display name
        if (!enchantLines.isEmpty()) {
            String base = meta.hasDisplayName()
                    ? meta.getDisplayName().replaceAll(" §7\\[§e\\d+ Enchants?§7\\]", "").trim()
                    : "";
            String label = enchantLines.size() == 1 ? "Enchant" : "Enchants";
            meta.setDisplayName(base + " §7[§e" + enchantLines.size() + " " + label + "§7]");
        }
        gear.setItemMeta(meta);
        return gear;
    }

    // ── Godly Transmog GUI helpers ────────────────────────────────────────────

    private void openGodlyTransmogGUI(Player player, ItemStack gear, int playerInvSlot) {
        List<String> enchantLines = new ArrayList<>();
        List<String> otherLines = new ArrayList<>();
        if (gear.hasItemMeta() && gear.getItemMeta().hasLore()) {
            for (String line : gear.getItemMeta().getLore()) {
                if (isCustomEnchantLine(line)) enchantLines.add(line);
                else otherLines.add(line);
            }
        }
        GodlyTransmogSession session = new GodlyTransmogSession(playerInvSlot, enchantLines, otherLines);
        godlyTransmogSessions.put(player.getUniqueId(), session);
        Inventory gui = Bukkit.createInventory(null, 27, GODLY_TRANSMOG_GUI_TITLE);
        refreshGodlyTransmogGUI(gui, session);
        player.openInventory(gui);
    }

    private void refreshGodlyTransmogGUI(Inventory gui, GodlyTransmogSession session) {
        // Filler
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fm = filler.getItemMeta();
        fm.setDisplayName("§r");
        filler.setItemMeta(fm);
        for (int i = 0; i < 27; i++) gui.setItem(i, filler);

        // Enchant buttons in slots 0–(n–1)
        for (int i = 0; i < session.enchantLines.size(); i++) {
            boolean selected = i == session.selectedGuiSlot;
            Material pane = selected ? Material.LIME_STAINED_GLASS_PANE : Material.PURPLE_STAINED_GLASS_PANE;
            ItemStack btn = new ItemStack(pane);
            ItemMeta bm = btn.getItemMeta();
            bm.setDisplayName(session.enchantLines.get(i));
            if (selected) {
                bm.setLore(List.of("§7(Selected) Click another enchant to swap"));
            } else if (session.selectedGuiSlot != -1) {
                bm.setLore(List.of("§7Click to swap with selected"));
            } else {
                bm.setLore(List.of("§7Click to select"));
            }
            btn.setItemMeta(bm);
            gui.setItem(i, btn);
        }

        // Confirm (slot 22)
        ItemStack confirm = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta cm = confirm.getItemMeta();
        cm.setDisplayName("§a§lConfirm");
        cm.setLore(List.of("§7Apply the current enchant order."));
        confirm.setItemMeta(cm);
        gui.setItem(22, confirm);

        // Cancel (slot 18)
        ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta can = cancel.getItemMeta();
        can.setDisplayName("§c§lCancel");
        can.setLore(List.of("§7Close without applying changes."));
        cancel.setItemMeta(can);
        gui.setItem(18, cancel);
    }

    private void applyGodlyTransmogOrder(Player player, GodlyTransmogSession session) {
        ItemStack storedItem = player.getInventory().getItem(session.playerInvSlot);
        if (storedItem == null || storedItem.getItemMeta() == null) return;
        ItemMeta meta = storedItem.getItemMeta();
        List<String> newLore = new ArrayList<>(session.otherLoreLines);
        newLore.addAll(session.enchantLines);
        meta.setLore(newLore);
        storedItem.setItemMeta(meta);
        player.getInventory().setItem(session.playerInvSlot, storedItem);
    }

    // ── Randomization Scroll helpers ──────────────────────────────────────────

    private int rerollSuccessRate(CustomEnchantment.EnchantTier tier) {
        if (tier == null) return ThreadLocalRandom.current().nextInt(40) + 50; // 50–89
        int min = plugin.getConfig().getInt("book-rates." + tier.name() + ".success-min", 50);
        int max = plugin.getConfig().getInt("book-rates." + tier.name() + ".success-max", 90);
        if (max <= min) return min;
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    private int rerollDestroyRate(CustomEnchantment.EnchantTier tier) {
        if (tier == null) return ThreadLocalRandom.current().nextInt(15);
        int min = plugin.getConfig().getInt("book-rates." + tier.name() + ".destroy-min", 0);
        int max = plugin.getConfig().getInt("book-rates." + tier.name() + ".destroy-max", 10);
        if (max <= min) return min;
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    /** Rebuilds an enchant book's lore and PDC to reflect new success/destroy rates. */
    private ItemStack rebuildBookRates(ItemStack book, int success, int destroy) {
        ItemStack result = book.clone();
        ItemMeta meta = result.getItemMeta();
        if (meta == null) return result;
        if (meta.hasLore()) {
            List<String> lore = new ArrayList<>(meta.getLore());
            lore.replaceAll(line -> {
                String stripped = line.replaceAll("§[0-9a-fk-or]", "").trim();
                if (stripped.startsWith("Success Rate:")) return "§aSuccess Rate: §f" + success + "%";
                if (stripped.startsWith("Destroy Rate:")) return "§cDestroy Rate: §f" + destroy + "%";
                return line;
            });
            meta.setLore(lore);
        }
        var pdc = meta.getPersistentDataContainer();
        pdc.set(EnchantBook.ENCHANT_SUCCESS_RATE_KEY, PersistentDataType.INTEGER, success);
        pdc.set(EnchantBook.ENCHANT_DESTROY_RATE_KEY, PersistentDataType.INTEGER, destroy);
        result.setItemMeta(meta);
        return result;
    }
}
