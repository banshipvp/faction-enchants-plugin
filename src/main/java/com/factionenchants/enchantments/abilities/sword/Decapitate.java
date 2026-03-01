package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

public class Decapitate extends CustomEnchantment {

    private final Random random = new Random();

    public Decapitate() {
        super("decapitate", "Decapitation", 3, EnchantTier.SIMPLE, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Victims have a chance of dropping their head on death.";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        if (!(victim instanceof Player playerVictim)) return;
        if (random.nextInt(100) < (level * 10)) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(playerVictim);
            head.setItemMeta(meta);
            victim.getWorld().dropItemNaturally(victim.getLocation(), head);
        }
    }
}
