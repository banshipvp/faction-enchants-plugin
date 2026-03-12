package com.factionenchants;

import com.factionenchants.items.SoulGemItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Tracks soul-gem active state per player and handles charge consumption.
 *
 * A player is "soul-active" when they have at least one Active Soul Gem anywhere
 * in their inventory.  Enchants call {@link #consumeSouls(Player, int)} which
 * drains from the first active gem found; if that gem empties it deactivates and
 * the system looks for the next one.
 */
public class SoulManager {

    /** Players whose soul gem is currently toggled ON. */
    private final Set<UUID> active = new HashSet<>();

    private final FactionEnchantsPlugin plugin;

    public SoulManager(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    // ── State ─────────────────────────────────────────────────────────────────

    /**
     * Toggles the soul gem currently held in main hand.
     * Returns the new active state, or null if the held item isn't a soul gem.
     */
    public Boolean toggleHeld(Player player) {
        ItemStack held = player.getInventory().getItemInMainHand();
        if (!SoulGemItem.isSoulGem(plugin, held)) return null;

        if (SoulGemItem.getCharges(plugin, held) <= 0) return null; // no charges, can't turn on

        boolean nowActive = SoulGemItem.toggle(plugin, held);
        player.getInventory().setItemInMainHand(held);

        if (nowActive) {
            active.add(player.getUniqueId());
        } else {
            // Check if any other gem in inventory is still active
            if (!hasAnyActiveGem(player)) {
                active.remove(player.getUniqueId());
            }
        }
        return nowActive;
    }

    /** Whether this player currently has soul enchants enabled. */
    public boolean isSoulActive(Player player) {
        if (!active.contains(player.getUniqueId())) return false;
        // Verify they still actually have an active gem in inventory
        if (!hasAnyActiveGem(player)) {
            active.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    /**
     * Consume {@code amount} soul charges from the player's active gem.
     * If the active gem runs out, it deactivates.  Returns true if the charges
     * were successfully consumed (i.e. the player had enough).
     */
    public boolean consumeSouls(Player player, int amount) {
        if (!active.contains(player.getUniqueId())) return false;

        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (!SoulGemItem.isSoulGem(plugin, item)) continue;
            if (!SoulGemItem.isActive(plugin, item)) continue;

            int before = SoulGemItem.getCharges(plugin, item);
            if (before < amount) continue; // not enough in this gem

            int remaining = SoulGemItem.drainCharges(plugin, item, amount);
            player.getInventory().setItem(i, item);
            player.updateInventory();

            // Play once per soul consumed, staggered by 1 tick each
            for (int tick = 0; tick < amount; tick++) {
                final int t = tick;
                plugin.getServer().getScheduler().runTaskLater(plugin,
                        () -> player.playSound(player.getLocation(), Sound.BLOCK_SOUL_SAND_BREAK, 0.45f, 1.8f), t);
            }

            // Instant action bar refresh if player is holding a soul gem
            sendHeldGemHud(player);

            if (remaining == 0) {
                active.remove(player.getUniqueId());
                player.sendMessage("§c✦ Your Soul Gem ran out of charges! Soul enchants deactivated.");
            }
            return true;
        }
        return false;
    }

    /**
     * Checks whether the player has enough soul charges (active gem with ≥ amount).
     */
    public boolean hasSouls(Player player, int amount) {
        ItemStack[] contents = player.getInventory().getContents();
        for (ItemStack item : contents) {
            if (!SoulGemItem.isSoulGem(plugin, item)) continue;
            if (!SoulGemItem.isActive(plugin, item)) continue;
            if (SoulGemItem.getCharges(plugin, item) >= amount) return true;
        }
        return false;
    }

    /** Called when a player leaves to clean up their state. */
    public void onQuit(Player player) {
        active.remove(player.getUniqueId());
    }

    /** Force-deactivate a player's active gem (e.g. on empty). */
    public void forceDeactivate(Player player) {
        active.remove(player.getUniqueId());
        // Also flip the active flag on any active gems in inventory
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (SoulGemItem.isSoulGem(plugin, item) && SoulGemItem.isActive(plugin, item)) {
                SoulGemItem.toggle(plugin, item); // toggles off
                player.getInventory().setItem(i, item);
            }
        }
        player.updateInventory();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean hasAnyActiveGem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SoulGemItem.isSoulGem(plugin, item) && SoulGemItem.isActive(plugin, item)
                    && SoulGemItem.getCharges(plugin, item) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sends an action bar showing the held soul gem's charges + state.
     * Call after charge mutations for instant HUD updates.
     */
    public void sendHeldGemHud(Player player) {
        ItemStack held = player.getInventory().getItemInMainHand();
        if (!SoulGemItem.isSoulGem(plugin, held)) return;
        int charges = SoulGemItem.getCharges(plugin, held);
        boolean isActive = SoulGemItem.isActive(plugin, held);
        String fmt = NumberFormat.getNumberInstance(Locale.US).format(charges);
        if (isActive) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§c✦ Soul Gem §a§lACTIVE §8| §c" + fmt + " §7souls remaining"));
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§c✦ Soul Gem §7" + fmt + " souls §8| §cINACTIVE"));
        }
    }
}
