package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Restore extends CustomEnchantment {

    private final Random random = new Random();

    public Restore() {
        super("restore", "Restore", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "When an item breaks, chance to lose one enchantment and repair it to 50% durability.";
    }

    @Override
    public void onActivate(Player player, int level, ItemStack item) {
        // Called by EnchantListener on PlayerItemBreakEvent
    }

    /**
     * Called from EnchantListener on PlayerItemBreakEvent.
     */
    public boolean tryRestore(Player player, ItemStack item) {
        if (random.nextInt(100) < level(item) * 15) {
            short maxDur = item.getType().getMaxDurability();
            org.bukkit.inventory.meta.Damageable meta = (org.bukkit.inventory.meta.Damageable) item.getItemMeta();
            if (meta != null) {
                meta.setDamage(maxDur / 2);
                item.setItemMeta(meta);
            }
            player.sendMessage("\u00a76Restore saved your item!");
            return true;
        }
        return false;
    }

    private int level(ItemStack item) { return 1; }
}
