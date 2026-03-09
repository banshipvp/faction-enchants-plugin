package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class DivineImmolation extends CustomEnchantment {

    public DivineImmolation() {
        super("divine_immolation", "Divine Immolation", 4, EnchantTier.SOUL, ApplicableGear.SWORD);
    }

    @Override
    public int getSoulCostPerTick() {
        return 0; // Soul cost is handled by the 4-tick fast timer in EnchantListener (1/tick = 5/sec)
    }

    @Override
    public String getDescription() {
        return "Sets nearby enemies ablaze and deals area damage\non right-click.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (Math.random() < 0.08) {
            double radius = 3.0 + level;
            Collection<Entity> nearby = attacker.getWorld().getNearbyEntities(attacker.getLocation(), radius, radius, radius);
            for (Entity entity : nearby) {
                if (entity instanceof LivingEntity living && !entity.equals(attacker)) {
                    living.setFireTicks(60 + level * 40);
                    living.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, level - 1));
                    living.damage(level * 2.0, attacker);
                }
            }
            attacker.sendMessage("§6§l✦ DIVINE IMMOLATION ✦ §eYou unleash a wave of holy fire!");
        }
    }
}
