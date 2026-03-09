package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.books.DustManager;
import com.factionenchants.books.EnchantBook;
import com.factionenchants.commands.TinkererCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TinkererListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public TinkererListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = event.getView().getTitle();

        if (!title.equals(TinkererCommand.GUI_TITLE)) return;

        Inventory top = event.getView().getTopInventory();
        int topSize = top.getSize();
        int rawSlot = event.getRawSlot();
        boolean inTop = rawSlot >= 0 && rawSlot < topSize;

        if (!inTop) {
            if (event.isShiftClick() || event.isLeftClick()) {
                ItemStack moved = event.getCurrentItem();
                if (isValidRecycleInput(moved)) {
                    event.setCancelled(true);
                    if (moveOneToFirstInputSlot(top, moved)) {
                        if (moved.getAmount() <= 0) event.setCurrentItem(null);
                        else event.setCurrentItem(moved);
                        schedulePreview(top);
                    }
                }
            }
            return;
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
            return;
        }

        if (TinkererCommand.INPUT_SLOTS.contains(rawSlot)) {
            // Left-click with empty cursor → return item to player inventory
            if (event.isLeftClick()
                    && (event.getCursor() == null || event.getCursor().getType() == Material.AIR)) {
                ItemStack item = top.getItem(rawSlot);
                if (item != null && item.getType() != Material.AIR) {
                    event.setCancelled(true);
                    top.setItem(rawSlot, null);
                    player.getInventory().addItem(item).values().forEach(leftover ->
                            player.getWorld().dropItemNaturally(player.getLocation(), leftover));
                    player.updateInventory();
                }
            }
            schedulePreview(top);
            return;
        }

        event.setCancelled(true);

        if (rawSlot == TinkererCommand.ADD_ALL_SLOT) {
            int moved = addAllFromPlayerInventory(player, top);
            if (moved > 0) {
                player.sendMessage("§aAdded §e" + moved + "§a item(s) to the tinkerer input.");
            } else {
                player.sendMessage("§cNo valid items found or input side is full.");
            }
            schedulePreview(top);
            return;
        }

        if (rawSlot == TinkererCommand.CONFIRM_SLOT) {
            processAllInputs(player, top);
            player.closeInventory();
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getView().getTitle().equals(TinkererCommand.GUI_TITLE)) return;
        Inventory top = event.getView().getTopInventory();

        boolean affectsTop = event.getRawSlots().stream().anyMatch(slot -> slot < top.getSize());
        if (!affectsTop) return;

        boolean allIntoInput = event.getRawSlots().stream()
                .filter(slot -> slot < top.getSize())
                .allMatch(TinkererCommand.INPUT_SLOTS::contains);

        if (!allIntoInput) {
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> updatePreview(top));
    }

    private void processAllInputs(Player player, Inventory top) {
        DustManager dustManager = plugin.getDustManager();
        int totalXp = 0;
        List<ItemStack> rewards = new ArrayList<>();

        for (int slot : TinkererCommand.INPUT_SLOTS) {
            ItemStack item = top.getItem(slot);
            if (item == null || item.getType() == Material.AIR) continue;

            if (EnchantBook.isEnchantBook(item) && !EnchantBook.isRandomBook(item)) {
                for (int i = 0; i < item.getAmount(); i++) {
                    ItemStack reward = dustManager.processEnchantBook(singleItem(item));
                    if (reward != null) rewards.add(reward);
                }
                top.setItem(slot, null);
                continue;
            }

            if (dustManager.canRecycleForXp(item)) {
                int amount = Math.max(1, item.getAmount());
                for (int i = 0; i < amount; i++) {
                    int xp = dustManager.processEnchantedGear(singleItem(item));
                    totalXp += xp;
                }
                top.setItem(slot, null);
            }
        }

        if (totalXp > 0) {
            rewards.add(dustManager.createStoredXpBottle(totalXp));
        }

        for (ItemStack reward : rewards) {
            player.getInventory().addItem(reward);
        }

        if (rewards.isEmpty() && totalXp == 0) {
            player.sendMessage("§cAdd enchant books or gear to recycle.");
        } else {
            String summary = totalXp > 0
                    ? "§e+1 Stored XP Bottle §7(contains " + totalXp + " XP)"
                    : "";
            player.sendMessage("§aRecycle complete! " + summary);
        }
    }

    private boolean isValidRecycleInput(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        return EnchantBook.isEnchantBook(item) || plugin.getDustManager().canRecycleForXp(item);
    }

    private boolean moveOneToFirstInputSlot(Inventory top, ItemStack moved) {
        for (int slot : TinkererCommand.INPUT_SLOTS) {
            ItemStack existing = top.getItem(slot);
            if (existing == null || existing.getType() == Material.AIR) {
                ItemStack one = moved.clone();
                one.setAmount(1);
                top.setItem(slot, one);
                moved.setAmount(moved.getAmount() - 1);
                return true;
            }
        }
        return false;
    }

    private void schedulePreview(Inventory top) {
        Bukkit.getScheduler().runTask(plugin, () -> updatePreview(top));
    }

    private void updatePreview(Inventory top) {
        for (int slot : TinkererCommand.OUTPUT_SLOTS) {
            top.setItem(slot, null);
        }

        ItemStack divider = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta dividerMeta = divider.getItemMeta();
        dividerMeta.setDisplayName(" ");
        divider.setItemMeta(dividerMeta);
        for (int slot : TinkererCommand.DIVIDER_SLOTS) {
            if (slot != TinkererCommand.CONFIRM_SLOT && slot != TinkererCommand.ADD_ALL_SLOT) {
                top.setItem(slot, divider);
            }
        }

        ItemStack addAll = new ItemStack(Material.HOPPER);
        ItemMeta addAllMeta = addAll.getItemMeta();
        addAllMeta.setDisplayName("§b§lAdd All");
        addAllMeta.setLore(Arrays.asList(
                "§7Move all valid enchant books/gear",
                "§7from your inventory to input slots",
                "",
                "§bClick to add all"
        ));
        addAll.setItemMeta(addAllMeta);
        top.setItem(TinkererCommand.ADD_ALL_SLOT, addAll);

        int index = 0;
        for (int slot : TinkererCommand.INPUT_SLOTS) {
            if (index >= TinkererCommand.OUTPUT_SLOTS.size()) break;
            ItemStack input = top.getItem(slot);
            if (input == null || input.getType() == Material.AIR) continue;

            ItemStack preview = buildPreview(input);
            top.setItem(TinkererCommand.OUTPUT_SLOTS.get(index), preview);
            index++;
        }
    }

    private ItemStack buildPreview(ItemStack input) {
        if (EnchantBook.isEnchantBook(input)) {
            var tier = EnchantBook.getTier(input);
            String tierName = tier != null ? tier.getDisplayName() : "Unknown";
            String color = tier != null ? "§" + tier.getColor() : "§7";
            int successRate = Math.max(0, Math.min(100, EnchantBook.getSuccessRate(input)));
            int maxPossible = plugin.getDustManager().getMaxRandomDustPercentFromSuccessRate(successRate);
            ItemStack preview = new ItemStack(Material.FIRE_CHARGE);
            ItemMeta meta = preview.getItemMeta();
            meta.setDisplayName(color + "§lRandom " + tierName + " Dust");
            meta.setLore(Arrays.asList(
                    "§7Reveals on right-click",
                    "§7Tier: " + color + tierName,
                    "§7Possible bonus: §e1% - " + maxPossible + "%",
                    "§7Can roll a §8gunpowder dud"
            ));
            preview.setItemMeta(meta);
            return preview;
        }

        int xp = plugin.getDustManager().processEnchantedGear(singleItem(input));
        ItemStack preview = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = preview.getItemMeta();
        meta.setDisplayName("§a§lXP Reward: §e" + xp);
        meta.setLore(List.of("§7You will receive §e" + xp + " XP"));
        preview.setItemMeta(meta);
        return preview;
    }

    private ItemStack singleItem(ItemStack item) {
        ItemStack clone = item.clone();
        clone.setAmount(1);
        return clone;
    }

    private int addAllFromPlayerInventory(Player player, Inventory top) {
        PlayerInventory playerInventory = player.getInventory();
        int movedItems = 0;

        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = playerInventory.getItem(slot);
            if (!isValidRecycleInput(item)) continue;

            ItemStack remaining = moveStackToInputSlots(top, item.clone());
            int moved = item.getAmount() - (remaining == null ? 0 : remaining.getAmount());
            if (moved <= 0) continue;

            movedItems += moved;
            if (remaining == null || remaining.getAmount() <= 0) {
                playerInventory.setItem(slot, null);
            } else {
                playerInventory.setItem(slot, remaining);
            }
        }

        return movedItems;
    }

    private ItemStack moveStackToInputSlots(Inventory top, ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR || stack.getAmount() <= 0) return stack;

        for (int slot : TinkererCommand.INPUT_SLOTS) {
            ItemStack existing = top.getItem(slot);
            if (existing == null || existing.getType() == Material.AIR) continue;
            if (!existing.isSimilar(stack)) continue;

            int max = existing.getMaxStackSize();
            int canAdd = max - existing.getAmount();
            if (canAdd <= 0) continue;

            int toAdd = Math.min(canAdd, stack.getAmount());
            existing.setAmount(existing.getAmount() + toAdd);
            stack.setAmount(stack.getAmount() - toAdd);
            if (stack.getAmount() <= 0) return null;
        }

        for (int slot : TinkererCommand.INPUT_SLOTS) {
            ItemStack existing = top.getItem(slot);
            if (existing != null && existing.getType() != Material.AIR) continue;

            int max = Math.min(stack.getMaxStackSize(), top.getMaxStackSize());
            int toPlace = Math.min(max, stack.getAmount());

            ItemStack placed = stack.clone();
            placed.setAmount(toPlace);
            top.setItem(slot, placed);

            stack.setAmount(stack.getAmount() - toPlace);
            if (stack.getAmount() <= 0) return null;
        }

        return stack;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = event.getItem();
        if (item == null) return;

        DustManager dustManager = plugin.getDustManager();
        if (dustManager.isStoredXpBottle(item)) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            int storedXp = dustManager.getStoredXpFromBottle(item);
            if (storedXp <= 0) return;
            consumeOne(player.getInventory(), event.getHand());
            player.giveExp(storedXp);
            player.sendMessage("§aYou redeemed §e" + storedXp + " XP§a from your bottle!");
            return;
        }

        if (!dustManager.isRandomDustToken(item)) return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        DustManager.DustOpenResult result = dustManager.openRandomDustToken(item);
        if (result == null) return;

        consumeOne(player.getInventory(), event.getHand());

        if (result.isDud()) {
            ItemStack dud = result.getReward();
            if (dud != null) {
                player.getInventory().addItem(dud);
            }
            player.sendMessage("§cYour random dust rolled a dud (gunpowder).");
            return;
        }

        ItemStack reward = result.getReward();
        if (reward != null) {
            player.getInventory().addItem(reward);
            player.sendMessage("§aYour random dust revealed real dust!");
        }
    }

    private void consumeOne(PlayerInventory inventory, org.bukkit.inventory.EquipmentSlot hand) {
        ItemStack stack = hand == org.bukkit.inventory.EquipmentSlot.OFF_HAND
                ? inventory.getItemInOffHand()
                : inventory.getItemInMainHand();
        if (stack == null || stack.getType() == Material.AIR) return;

        if (stack.getAmount() > 1) {
            stack.setAmount(stack.getAmount() - 1);
            if (hand == org.bukkit.inventory.EquipmentSlot.OFF_HAND) inventory.setItemInOffHand(stack);
            else inventory.setItemInMainHand(stack);
        } else {
            if (hand == org.bukkit.inventory.EquipmentSlot.OFF_HAND) inventory.setItemInOffHand(null);
            else inventory.setItemInMainHand(null);
        }
    }
}
