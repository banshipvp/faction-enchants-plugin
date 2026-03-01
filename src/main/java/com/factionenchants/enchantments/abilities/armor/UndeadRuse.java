package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class UndeadRuse extends CustomEnchantment {

    private final Random random = new Random();

    public UndeadRuse() {
        super("undead_ruse", "Undead Ruse", 10, EnchantTier.ELITE, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "When hit you have a chance to spawn zombie hordes to distract and disorient your opponents.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (random.nextInt(100) < level * 5) {
            int count = Math.min(level / 3 + 1, 3);
            for (int i = 0; i < count; i++) {
                double ox = (random.nextDouble() - 0.5) * 6;
                double oz = (random.nextDouble() - 0.5) * 6;
                Zombie z = defender.getWorld().spawn(defender.getLocation().add(ox, 0, oz), Zombie.class);
                if (attacker instanceof LivingEntity le) {
                    z.setTarget(le);
                }
            }
        }
    }
}
