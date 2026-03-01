package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class Shuffle extends CustomEnchantment {

    private final Random random = new Random();

    public Shuffle() {
        super("shuffle", "Shuffle", 3, EnchantTier.SIMPLE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Shuffles opponent's hotbar.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;
        if (random.nextInt(100) < level * 8) {
            PlayerInventory inv = victim.getInventory();
            for (int i = 0; i < 6; i++) {
                int a = random.nextInt(9);
                int b = random.nextInt(9);
                ItemStack tmp = inv.getItem(a);
                inv.setItem(a, inv.getItem(b));
                inv.setItem(b, tmp);
            }
            victim.sendMessage("\u00a7cYour hotbar has been shuffled!");
        }
    }
}
