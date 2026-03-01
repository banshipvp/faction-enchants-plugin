package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Blacksmith extends CustomEnchantment {

    public Blacksmith() {
        super("blacksmith", "Blacksmith", 5, EnchantTier.LEGENDARY, ApplicableGear.TOOL_ALL);
    }

    @Override
    public String getDescription() {
        return "Chance to repair your held item when mining blocks.";
    }

    // Called by EnchantListener on BlockBreakEvent
    public void onBlockBreak(Player player, BlockBreakEvent event, int level) {
        if (Math.random() * 100 < level * 5) {
            ItemStack held = player.getInventory().getItemInMainHand();
            org.bukkit.inventory.meta.Damageable meta = (org.bukkit.inventory.meta.Damageable) held.getItemMeta();
            if (meta != null && meta.getDamage() > 0) {
                meta.setDamage(Math.max(0, meta.getDamage() - level * 5));
                held.setItemMeta(meta);
            }
        }
    }
}
