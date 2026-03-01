package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WebWalker extends CustomEnchantment {

    public WebWalker() {
        super("web_walker", "Web Walker", 5, EnchantTier.MASTERY, ApplicableGear.BOOTS);
    }

    @Override
    public String getDescription() {
        return "Walks through webs and cobwebs instantly.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, level + 1, true, false));
    }

    @Override
    public void onHurtBy(Player defender, org.bukkit.entity.Entity attacker, int level, org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL) {
            event.setDamage(0);
        }
    }
}
