package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Stormcaller extends CustomEnchantment {

    private final Random random = new Random();

    public Stormcaller() {
        super("stormcaller", "Stormcaller", 4, EnchantTier.ELITE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Strikes lightning on attacking players.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        if (!(attacker instanceof Player)) return;
        if (random.nextInt(100) < level * 12) {
            attacker.getWorld().strikeLightning(attacker.getLocation());
        }
    }
}
