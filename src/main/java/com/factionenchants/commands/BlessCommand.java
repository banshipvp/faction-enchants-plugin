package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * /bless — available to all players.
 * Instantly removes Slowness and Mining Fatigue from the player.
 */
public class BlessCommand implements CommandExecutor {

    private final FactionEnchantsPlugin plugin;

    public BlessCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    /** Returns true if the player is currently wearing any Drunk-enchanted armour. */
    private boolean isWearingDrunk(Player player) {
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType().isAir()) continue;
            for (CustomEnchantment enc : plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).keySet()) {
                if ("drunk".equals(enc.getId())) return true;
            }
        }
        return false;
    }

    /**
     * Players in this set have Slowness and Mining Fatigue blocked indefinitely.
     * Managed by EnchantListener's per-tick loop — when a drunk helmet is
     * equipped the player moves to WAS_BLESSED; when removed they move back.
     */
    public static final Set<UUID> BLESSED     = Collections.synchronizedSet(new HashSet<>());

    /**
     * Players who were blessed before equipping a drunk helmet. They will
     * automatically return to BLESSED when the drunk helmet is removed.
     */
    public static final Set<UUID> WAS_BLESSED = Collections.synchronizedSet(new HashSet<>());

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        boolean hadSlow    = player.hasPotionEffect(PotionEffectType.SLOW);
        boolean hadFatigue = player.hasPotionEffect(PotionEffectType.SLOW_DIGGING);

        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);

        // Mark this player as immune to Slowness and Mining Fatigue for this session
        boolean alreadyBlessed = !BLESSED.add(player.getUniqueId());

        if (hadSlow || hadFatigue) {
            player.sendMessage("§a§l✦ Blessed! §r§aDebuffs cleansed and §fShield of Clarity §aactivated.");
            if (hadSlow)    player.sendMessage("§7  - §fSlowness §7removed");
            if (hadFatigue) player.sendMessage("§7  - §fMining Fatigue §7removed");
        } else if (alreadyBlessed) {
            player.sendMessage("§7You are already blessed. Slowness and Mining Fatigue remain blocked.");
        } else {
            player.sendMessage("§a§l✦ Blessed! §r§aNo debuffs present — §fShield of Clarity §aactivated.");
        }
        if (!alreadyBlessed) {
            player.sendMessage("§7Slowness and Mining Fatigue are blocked while this blessing is active.");
        }

        return true;
    }
}
