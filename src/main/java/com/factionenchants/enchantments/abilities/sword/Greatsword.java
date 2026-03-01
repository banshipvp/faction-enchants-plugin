package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Greatsword extends CustomEnchantment {

    private final Random random = new Random();

    public Greatsword() {
        super("greatsword", "Greatsword", 5, EnchantTier.ELITE, ApplicableGear.SWORD);
    }

    @Override
    public String getDescription() {
        return "Multiplies damage against players who are wielding a BOW at the time they are hit.";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;
        String held = victim.getInventory().getItemInMainHand().getType().name();
        if (held.equals("BOW") || held.equals("CROSSBOW")) {
            event.setDamage(event.getDamage() * (1 + level * 0.2));
        }
    }
}
