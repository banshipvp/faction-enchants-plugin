package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.books.DustManager;
import com.factionenchants.books.EnchantBook;
import com.factionenchants.commands.AlchemistCommand;
import com.factionenchants.commands.EnchanterCommand;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.EnchantmentManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BookListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public BookListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = event.getView().getTitle();

        // ---- Enchanter GUI ----
        if (title.equals(EnchanterCommand.GUI_TITLE)) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            var pdc = clicked.getItemMeta().getPersistentDataContainer();
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

        if (isBlackScroll(cursor)) {
            event.setCancelled(true);

            Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(current);
            if (enchants.isEmpty()) {
                player.sendMessage("§cThat item has no custom enchants to extract.");
                return;
            }

            List<Map.Entry<CustomEnchantment, Integer>> entries = new ArrayList<>(enchants.entrySet());
            Map.Entry<CustomEnchantment, Integer> extracted = entries.get(ThreadLocalRandom.current().nextInt(entries.size()));

            ItemStack updatedTarget = current.clone();
            plugin.getEnchantmentManager().removeEnchantment(updatedTarget, extracted.getKey());
            if (event.getClickedInventory() != null) {
                event.getClickedInventory().setItem(event.getSlot(), updatedTarget);
            } else {
                event.getView().setItem(event.getRawSlot(), updatedTarget);
            }

            ItemStack extractedBook = EnchantBook.createSpecificBook(extracted.getKey(), extracted.getValue(), 100, 0);
            player.getInventory().addItem(extractedBook).values().forEach(leftover ->
                    player.getWorld().dropItemNaturally(player.getLocation(), leftover));

            if (cursor.getAmount() > 1) {
                cursor.setAmount(cursor.getAmount() - 1);
                event.getView().setCursor(cursor);
            } else {
                event.getView().setCursor(null);
            }

            player.updateInventory();
            player.sendMessage("§aBlack Scroll extracted §e" + extracted.getKey().getDisplayName() + " " + EnchantmentManager.toRoman(extracted.getValue()) + "§a.");
            return;
        }

        DustManager dustManager = plugin.getDustManager();
        if (dustManager.isMysteryDust(cursor)) {
            event.setCancelled(true);

            if (!EnchantBook.isEnchantBook(current) || EnchantBook.isRandomBook(current)) {
                player.sendMessage("§cYou can only apply dust to a revealed enchant book.");
                return;
            }

            CustomEnchantment.EnchantTier dustTier = dustManager.getDustTier(cursor);
            CustomEnchantment.EnchantTier bookTier = EnchantBook.getTier(current);
            if (dustTier == null || bookTier == null || dustTier != bookTier) {
                player.sendMessage("§cDust tier must match the enchant book tier.");
                return;
            }

            DustManager.DustApplyResult result = dustManager.applyDustToBook(cursor, current);
            if (result == null) {
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

            player.sendMessage("§aDust applied! §e+" + result.getAppliedBoost() + "% §asuccess (now §e" + result.getNewSuccessRate() + "%§a).");
            return;
        }

        if (!EnchantBook.isEnchantBook(cursor) || EnchantBook.isRandomBook(cursor)) return;

        // cursor is a specific enchant book, current is the target gear
        String enchantId = EnchantBook.getEnchantId(cursor);
        int level = EnchantBook.getEnchantLevel(cursor);
        CustomEnchantment enchant = plugin.getEnchantmentManager().getEnchantment(enchantId);
        if (enchant == null) return;
        if (!enchant.canApplyTo(current)) {
            event.setCancelled(true);
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
                player.sendMessage("\u00a7cThis enchant requires \u00a7e" + prereq + " \u00a7cto be applied first!");
                return;
            }
        }
        if (!existing.containsKey(enchant) && existing.size() >= EnchantBook.MAX_ENCHANTS_PER_ITEM) {
            event.setCancelled(true);
            player.sendMessage("\u00a7cThis item already has the maximum of \u00a7e" + EnchantBook.MAX_ENCHANTS_PER_ITEM + " \u00a7cenchants!");
            return;
        }
        if (existing.containsKey(enchant) && existing.get(enchant) >= enchant.getMaxLevel()) {
            event.setCancelled(true);
            player.sendMessage("\u00a7cThis enchant is already at max level!");
            return;
        }

        event.setCancelled(true);
        int successRate = Math.max(0, Math.min(100, EnchantBook.getSuccessRate(cursor)));
        int destroyRate = Math.max(0, Math.min(100, EnchantBook.getDestroyRate(cursor)));
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
                event.getView().setItem(event.getRawSlot(), null);
                player.sendMessage("\u00a7cEnchant failed and your gear was destroyed!");
            } else {
                player.sendMessage("\u00a7cEnchant failed! Your gear survived.");
            }
        }

        // Consume one book from cursor
        if (cursor.getAmount() > 1) {
            cursor.setAmount(cursor.getAmount() - 1);
            event.getView().setCursor(cursor);
        } else {
            event.getView().setCursor(null);
        }
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
        if (!event.getView().getTitle().equals(AlchemistCommand.GUI_TITLE)) return;
        if (!(event.getPlayer() instanceof Player player)) return;

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

    private boolean isBlackScroll(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }

        String strippedName = meta.getDisplayName().replaceAll("§[0-9a-fk-or]", "").trim().toLowerCase();
        return strippedName.contains("black scroll");
    }
}
