package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.items.SoulGemItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

/**
 * Handles Soul Gem toggle (right-click) and Random Soul Gem Generator (right-click).
 * Also runs a per-second task for passive soul-drain enchants.
 */
public class SoulGemListener implements Listener {

    private static final Random RANDOM = new Random();

    private final FactionEnchantsPlugin plugin;

    public SoulGemListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        // Repeating HUD ticker — shows action bar while holding a soul gem
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                plugin.getSoulManager().sendHeldGemHud(p);
            }
        }, 2L, 3L);
    }

    // ── Toggle ────────────────────────────────────────────────────────────────

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack held = player.getInventory().getItemInMainHand();

        // ── Random Soul Gem Generator ──────────────────────────────────────
        if (SoulGemItem.isGenerator(plugin, held)) {
            event.setCancelled(true);
            int charges = 100 + RANDOM.nextInt(4901); // 100–5000
            ItemStack gem = SoulGemItem.create(plugin, charges);

            var leftovers = player.getInventory().addItem(gem);
            leftovers.values().forEach(l -> player.getWorld().dropItemNaturally(player.getLocation(), l));

            String fmt = NumberFormat.getNumberInstance(Locale.US).format(charges);
            player.sendMessage("§c✦ §7You received a §c§lSoul Gem §7with §c" + fmt + " §7charges!");
            player.updateInventory();
            return;
        }

        // ── Soul Gem toggle ────────────────────────────────────────────────
        if (!SoulGemItem.isSoulGem(plugin, held)) return;
        event.setCancelled(true);

        int charges = SoulGemItem.getCharges(plugin, held);
        if (charges <= 0) {
            player.sendMessage("§c✦ This Soul Gem has no charges left!");
            return;
        }

        Boolean nowActive = plugin.getSoulManager().toggleHeld(player);
        if (nowActive == null) return;

        String fmt = NumberFormat.getNumberInstance(Locale.US).format(charges);

        if (nowActive) {
            sendActionBar(player, "§c✦ Soul Gem §a§lON §8| §c" + fmt + " §7souls remaining");
            player.sendMessage("§c✦ §aSoul Gem §aactivated§a! SOUL enchants are now §a§lENABLED§a. (§c" + fmt + " §7charges)");
        } else {
            sendActionBar(player, "§c✦ Soul Gem §c§lOFF");
            player.sendMessage("§c✦ §cSoul Gem deactivated. SOUL enchants are §c§lDISABLED§c.");
        }
    }

    // ── Combine two soul gems by drag-and-drop ────────────────────────────────

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack cursor  = event.getCursor();
        ItemStack clicked = event.getCurrentItem();

        if (cursor  == null || cursor.getType()  == Material.AIR) return;
        if (clicked == null || clicked.getType() == Material.AIR) return;

        // Both must be soul gems but NOT generators
        if (!SoulGemItem.isSoulGem(plugin, cursor))  return;
        if (!SoulGemItem.isSoulGem(plugin, clicked)) return;
        if (SoulGemItem.isGenerator(plugin, cursor) || SoulGemItem.isGenerator(plugin, clicked)) return;

        event.setCancelled(true);

        int total = SoulGemItem.getCharges(plugin, cursor) + SoulGemItem.getCharges(plugin, clicked);
        // Preserve active state of the slot gem
        SoulGemItem.setCharges(plugin, clicked, total);
        event.setCurrentItem(clicked);
        player.setItemOnCursor(new ItemStack(Material.AIR));
        player.updateInventory();

        String fmt = NumberFormat.getNumberInstance(Locale.US).format(total);
        player.sendMessage("§c✦ §7Soul Gems combined! §c" + fmt + " §7total charges.");
    }

    // ── Cleanup on quit ───────────────────────────────────────────────────────

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getSoulManager().onQuit(event.getPlayer());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void sendActionBar(Player player, String legacyText) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(legacyText));
    }
}
