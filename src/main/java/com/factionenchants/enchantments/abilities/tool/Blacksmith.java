package com.factionenchants.enchantments.abilities.tool;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Random;

/**
 * Blacksmith V — Axe enchantment.
 * Chance to heal the most damaged armor piece by 1-2 durability on player hit,
 * but when it procs the attack only deals 50% damage.
 */
public class Blacksmith extends CustomEnchantment {

    private static final Random random = new Random();

    public Blacksmith() {
        super("blacksmith", "Blacksmith", 5, EnchantTier.LEGENDARY, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Chance to heal your most damaged piece of armor by 1-2 durability whenever you hit a player, "
             + "but when it procs your attack will only deal 50% of the normal damage.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player)) return;
        // Proc chance: 10% per level (10%–50%)
        if (random.nextInt(100) >= level * 10) return;

        // Find the most damaged armor piece
        ItemStack[] armor = attacker.getInventory().getArmorContents();
        ItemStack mostDamaged = null;
        int highestDamage = 0;
        for (ItemStack piece : armor) {
            if (piece == null || !(piece.getItemMeta() instanceof Damageable dm)) continue;
            if (dm.getDamage() > highestDamage) {
                highestDamage = dm.getDamage();
                mostDamaged = piece;
            }
        }
        if (mostDamaged == null || highestDamage == 0) return;

        // Heal 1-2 durability
        int repairAmount = 1 + random.nextInt(2);
        Damageable meta = (Damageable) mostDamaged.getItemMeta();
        meta.setDamage(Math.max(0, meta.getDamage() - repairAmount));
        mostDamaged.setItemMeta(meta);

        // Reduce attack damage to 50%
        event.setDamage(event.getDamage() * 0.5);
    }
}
