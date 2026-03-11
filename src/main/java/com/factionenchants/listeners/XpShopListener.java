package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.commands.XpShopCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Locale;

public class XpShopListener implements Listener {

    private final FactionEnchantsPlugin plugin;
    private final XpShopCommand xpShopCommand;

    public XpShopListener(FactionEnchantsPlugin plugin, XpShopCommand xpShopCommand) {
        this.plugin = plugin;
        this.xpShopCommand = xpShopCommand;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(XpShopCommand.GUI_TITLE)) return;

        event.setCancelled(true);
        event.setResult(org.bukkit.event.inventory.InventoryClickEvent.Result.DENY);

        ItemStack clicked = event.getCurrentItem();
        String itemId = XpShopCommand.getShopItemId(clicked);
        Integer cost = XpShopCommand.getShopItemCost(clicked);
        if (itemId == null || cost == null) return;

        int playerXp = getPlayerTotalXp(player);
        if (playerXp < cost) {
            player.sendMessage("§cYou need §e" + format(cost) + " XP §cto buy this item. You currently have §e" + format(playerXp) + " XP§c.");
            return;
        }

        ItemStack purchasedItem = xpShopCommand.createPurchasedItem(itemId);
        boolean gaveViaSimpleFactions = tryGiveViaSimpleFactions(player, itemId);

        boolean wandPurchase = XpShopCommand.ITEM_SELL_WAND.equals(itemId) || XpShopCommand.ITEM_TNT_WAND.equals(itemId);
        if (wandPurchase && !gaveViaSimpleFactions && purchasedItem == null) {
            player.sendMessage("§cCould not issue the correct SimpleFactions wand. No XP was charged.");
            return;
        }

        if (!gaveViaSimpleFactions && purchasedItem == null) {
            player.sendMessage("§cShop item is unavailable right now.");
            return;
        }

        setPlayerTotalXp(player, playerXp - cost);

        if (gaveViaSimpleFactions) {
            player.sendMessage("§aPurchased item for §e" + format(cost) + " XP§a.");
            return;
        }

        // Ensure exactly 1 item
        if (purchasedItem != null) {
            purchasedItem.setAmount(1);
            player.getInventory().addItem(purchasedItem);
        }
        player.sendMessage("§aPurchased item for §e" + format(cost) + " XP§a.");
    }

    private boolean tryGiveViaSimpleFactions(Player player, String itemId) {
        String path;
        String defaultPlayerCommand;
        if (XpShopCommand.ITEM_SELL_WAND.equals(itemId)) {
            path = "xpshop.simplefactions.sell-wand-command";
            defaultPlayerCommand = "f sellwand";
        } else if (XpShopCommand.ITEM_TNT_WAND.equals(itemId)) {
            path = "xpshop.simplefactions.tnt-wand-command";
            defaultPlayerCommand = "f tnt wand";
        } else {
            return false;
        }

        if (!plugin.getConfig().getBoolean("xpshop.simplefactions.enabled", false)) {
            return false;
        }

        String commandTemplate = plugin.getConfig().getString(path, defaultPlayerCommand).trim();
        if (commandTemplate.isEmpty()) {
            return false;
        }

        boolean consoleMode = commandTemplate.contains("{player}") || commandTemplate.contains("%player%");

        String command = commandTemplate
                .replace("{player}", player.getName())
                .replace("%player%", player.getName());

        String normalized = command.startsWith("/") ? command.substring(1) : command;

        if (consoleMode) {
            return plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), normalized);
        }
        return player.performCommand(normalized);
    }

    private String format(int value) {
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }

    private int getPlayerTotalXp(Player player) {
        int level = player.getLevel();
        float progress = player.getExp();
        int current = getTotalXpForLevel(level);
        int toNext = player.getExpToLevel();
        return current + Math.round(progress * toNext);
    }

    private void setPlayerTotalXp(Player player, int totalXp) {
        int safeXp = Math.max(0, totalXp);
        player.setExp(0f);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.giveExp(safeXp);
    }

    private int getTotalXpForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        }
        if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }
        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }
}
