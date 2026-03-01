package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PlagueCarrier extends CustomEnchantment {

    private final Random random = new Random();

    public PlagueCarrier() {
        super("plague_carrier", "Plague Carrier", 8, EnchantTier.UNIQUE, ApplicableGear.LEGGINGS);
    }

    @Override
    public String getDescription() {
        return "When near death summons creepers and debuffs to avenge you.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (defender.getHealth() - event.getFinalDamage() < 3.0 && random.nextInt(100) < level * 6) {
            Location loc = defender.getLocation();
            int count = Math.min(level / 2 + 1, 4);
            for (int i = 0; i < count; i++) {
                double ox = (random.nextDouble() - 0.5) * 4;
                double oz = (random.nextDouble() - 0.5) * 4;
                loc.getWorld().spawn(loc.clone().add(ox, 0, oz), Creeper.class);
            }
            if (attacker instanceof Player enemy) {
                enemy.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                enemy.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                enemy.sendMessage("\u00a7cYou have been afflicted by the Plague Carrier!");
            }
        }
    }
}
