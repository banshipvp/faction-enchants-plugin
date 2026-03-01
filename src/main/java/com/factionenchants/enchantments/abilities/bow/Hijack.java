package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Hijack extends CustomEnchantment {

    private final Random random = new Random();

    public Hijack() {
        super("hijack", "Hijack", 4, EnchantTier.ELITE, ApplicableGear.BOW, ApplicableGear.CROSSBOW);
    }

    @Override
    public String getDescription() {
        return "Chance to convert summoned enemy Guardians into your own when they are shot with an arrow.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (target.getType() == org.bukkit.entity.EntityType.GUARDIAN || target.getType() == org.bukkit.entity.EntityType.ELDER_GUARDIAN) {
            if (random.nextInt(100) < level * 12) {
                event.setCancelled(true);
                target.setHealth(target.getMaxHealth());
                shooter.sendMessage("\u00a79You have hijacked a Guardian!");
            }
        }
    }
}
