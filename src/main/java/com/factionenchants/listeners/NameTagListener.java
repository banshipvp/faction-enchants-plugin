package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.items.NameTagItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles the custom Name Tag item:
 *  1. Right-click consumes the tag and starts a rename session.
 *  2. The player types a name in chat (supports & colour codes).
 *  3. A preview is displayed.
 *  4. Player types confirm / redo / cancel.
 */
public class NameTagListener implements Listener {

    // ── Session ───────────────────────────────────────────────────────────────

    private enum State { WAITING_INPUT, WAITING_CONFIRM }

    private static class Session {
        State state = State.WAITING_INPUT;
        String pendingName = null; // colour-translated name, ready to apply
    }

    private final FactionEnchantsPlugin plugin;
    private final Map<UUID, Session> sessions = new HashMap<>();

    public NameTagListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    // ── Right-click: start session ────────────────────────────────────────────

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        // Only fire once (main hand)
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack held = player.getInventory().getItemInMainHand();
        if (!NameTagItem.isNameTag(plugin, held)) return;

        event.setCancelled(true);

        // Consume one name tag
        if (held.getAmount() > 1) {
            held.setAmount(held.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }

        sessions.put(player.getUniqueId(), new Session());
        promptInput(player);
    }

    // ── Chat: handle session input ────────────────────────────────────────────

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Session session = sessions.get(event.getPlayer().getUniqueId());
        if (session == null) return;

        // Suppress the message from public chat
        event.setCancelled(true);

        Player player = event.getPlayer();
        String message = event.getMessage().trim();

        // Jump to main thread for inventory / item operations
        plugin.getServer().getScheduler().runTask(plugin, () -> process(player, session, message));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        sessions.remove(event.getPlayer().getUniqueId());
    }

    // ── Session logic (runs on main thread) ───────────────────────────────────

    private void process(Player player, Session session, String message) {
        // Guard: session might have been removed while task was queued
        if (!sessions.containsKey(player.getUniqueId())) return;

        String lower = message.toLowerCase();

        if (session.state == State.WAITING_INPUT) {
            // ---- cancel ----
            if (lower.equals("cancel")) {
                cancelSession(player, true);
                return;
            }

            // ---- translate colour codes (&a, &l, etc.) then §-codes ----
            String translated = ChatColor.translateAlternateColorCodes('&', message);
            session.pendingName = translated;
            session.state = State.WAITING_CONFIRM;
            showPreview(player, translated);

        } else { // WAITING_CONFIRM
            switch (lower) {
                case "confirm" -> {
                    ItemStack held = player.getInventory().getItemInMainHand();
                    if (held == null || held.getType().isAir()) {
                        player.sendMessage("§c§lName Tag §r§cYou aren't holding an item!");
                        player.sendMessage("§7Hold the item you want to rename, then type §aconfirm§7.");
                        return; // stay in WAITING_CONFIRM
                    }
                    applyName(player, held, session.pendingName);
                    sessions.remove(player.getUniqueId());
                }
                case "redo" -> {
                    session.state = State.WAITING_INPUT;
                    session.pendingName = null;
                    player.sendMessage("");
                    player.sendMessage("§6§l[Name Tag] §7Let's try again — type your new item name:");
                    player.sendMessage("§7Type §ccancel §7to cancel and get your Name Tag back.");
                    player.sendMessage("");
                }
                case "cancel" -> cancelSession(player, true);
                default -> player.sendMessage("§cType §aconfirm§c, §eredo§c or §ccancel§c.");
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void promptInput(Player player) {
        player.sendMessage("");
        player.sendMessage("§6§l[Name Tag]");
        player.sendMessage("§7Please type your custom item name in chat.");
        player.sendMessage("§7Use §f&<code> §7for colours — e.g. §a&aGreen §8| §c&4Dark Red §8| §b&bAqua");
        player.sendMessage("§7Bold: §l&l §r§7  Italic: §o&o §r§7  Underline: §n&n §r§7  Reset: §7&r");
        player.sendMessage("§7Type §ccancel §7to cancel.");
        player.sendMessage("");
    }

    private void showPreview(Player player, String translatedName) {
        ItemStack held = player.getInventory().getItemInMainHand();
        String itemLabel = held == null || held.getType().isAir()
                ? "§7(nothing — hold the item you want to rename before confirming)"
                : "§7" + formatMaterial(held.getType().name());

        player.sendMessage("");
        player.sendMessage("§6§l[Name Tag] §7Preview:");
        player.sendMessage("§8  Name: §r" + translatedName);
        player.sendMessage("§8  Will rename: " + itemLabel);
        player.sendMessage("");
        player.sendMessage("§8▶ §aType §2confirm §ato apply");
        player.sendMessage("§8▶ §eType §6redo §eto retype your name");
        player.sendMessage("§8▶ §cType §4cancel §cto cancel and get your Name Tag back");
        player.sendMessage("");
    }

    private void applyName(Player player, ItemStack item, String displayName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        player.updateInventory();
        player.sendMessage("");
        player.sendMessage("§a§l[Name Tag] §r§aApplied! Your item is now named: §r" + displayName);
        player.sendMessage("");
    }

    private void cancelSession(Player player, boolean returnTag) {
        sessions.remove(player.getUniqueId());
        if (returnTag) {
            ItemStack tag = NameTagItem.create(plugin);
            player.getInventory().addItem(tag)
                    .values()
                    .forEach(leftover -> player.getWorld().dropItemNaturally(player.getLocation(), leftover));
        }
        player.sendMessage("§c[Name Tag] §7Cancelled. Your Name Tag has been returned.");
    }

    private String formatMaterial(String rawName) {
        String[] words = rawName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!sb.isEmpty()) sb.append(' ');
            sb.append(Character.toUpperCase(w.charAt(0)));
            sb.append(w.substring(1).toLowerCase());
        }
        return sb.toString();
    }
}
