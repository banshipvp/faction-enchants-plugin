package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Teleblock extends CustomEnchantment {

    /** UUID → expiry System.currentTimeMillis() */
    private static final Map<UUID, Long> teleblocked = new HashMap<>();

    private static final long DURATION_MS = 30_000L; // 30 seconds
    private static final int MAX_PEARL_REMOVE = 15;

    public Teleblock() {
        super("teleblock", "Teleblock", 5, EnchantTier.SOUL, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Active Soul Enchantment. Your bow is enchanted with enderpearl blocking magic, damaged players will be unable to use enderpearls for up to 30 seconds, and will lose up to 15 enderpearls from their inventory. Costs level x 6 souls per shot.";
    }

    @Override
    public int getSoulCostPerProc() {
        // Handled manually in onArrowHit to use level-based cost
        return 0;
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;

        com.factionenchants.FactionEnchantsPlugin plugin =
                (com.factionenchants.FactionEnchantsPlugin) org.bukkit.Bukkit.getPluginManager().getPlugin("FactionEnchants");
        if (plugin == null) return;
        if (!plugin.getSoulManager().isSoulActive(shooter)) return;
        if (!plugin.getSoulManager().consumeSouls(shooter, level * 6)) return;

        // Apply teleblock
        teleblocked.put(victim.getUniqueId(), System.currentTimeMillis() + DURATION_MS);

        // Remove up to 15 enderpearls
        int removed = 0;
        ItemStack[] contents = victim.getInventory().getContents();
        for (int i = 0; i < contents.length && removed < MAX_PEARL_REMOVE; i++) {
            ItemStack stack = contents[i];
            if (stack == null || stack.getType() != Material.ENDER_PEARL) continue;
            int take = Math.min(stack.getAmount(), MAX_PEARL_REMOVE - removed);
            removed += take;
            if (take >= stack.getAmount()) {
                victim.getInventory().setItem(i, null);
            } else {
                stack.setAmount(stack.getAmount() - take);
            }
        }
        victim.updateInventory();

        victim.sendMessage("\u00a7c\u2736 You have been Teleblocked for 30 seconds!");
        victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0f, 1.5f);
        shooter.sendMessage("\u00a7a\u2736 Teleblock hit! Enderpearls removed: \u00a7e" + removed);
    }

    /** Check whether a player is currently teleblocked. Used by TeleblockListener. */
    public static boolean isTeleblocked(UUID uuid) {
        Long expiry = teleblocked.get(uuid);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            teleblocked.remove(uuid);
            return false;
        }
        return true;
    }
}
