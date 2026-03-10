package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Disarmor VIII — Sword enchantment (LEGENDARY).
 * When hitting a player who is below 30% health, there is a (level * 2)%
 * chance to strip one random piece of armor from the target. The armor piece
 * is dropped at the target's feet.
 */
public class Disarmor extends CustomEnchantment {

    /** Health threshold (30% of max health). */
    private static final double LOW_HEALTH_FRACTION = 0.30;

    public Disarmor() {
        super("disarmor", "Disarmor", 8, EnchantTier.LEGENDARY, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "A slight chance of removing one piece of armor from your enemy when they are at low health.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;

        double healthFraction = victim.getHealth() / victim.getMaxHealth();
        if (healthFraction > LOW_HEALTH_FRACTION) return;

        double procChance = level * 0.02; // 2% per level, up to 16% at VIII
        if (Math.random() >= procChance) return;

        PlayerInventory inv = victim.getInventory();
        ItemStack[] armor = inv.getArmorContents();

        // Collect non-null slots: 0=boots, 1=legs, 2=chest, 3=helmet
        List<Integer> occupied = new ArrayList<>();
        for (int i = 0; i < armor.length; i++) {
            if (armor[i] != null && armor[i].getType().isItem()) occupied.add(i);
        }
        if (occupied.isEmpty()) return;

        int slot = occupied.get((int) (Math.random() * occupied.size()));
        ItemStack stripped = armor[slot];
        armor[slot] = null;
        inv.setArmorContents(armor);

        // Drop the item at the target's feet
        target.getWorld().dropItemNaturally(target.getLocation(), stripped);
    }
}
