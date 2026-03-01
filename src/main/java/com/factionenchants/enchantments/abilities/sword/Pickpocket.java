package com.factionenchants.enchantments.abilities.sword;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Pickpocket extends CustomEnchantment {

    private final Random random = new Random();

    public Pickpocket() {
        super("pickpocket", "Pickpocket", 3, EnchantTier.ULTIMATE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Steals money from your opponent (if economy is installed).";
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        // Economy hook placeholder — integrate with Vault/CMI if available
        if (target instanceof Player victim) {
            attacker.sendMessage("\u00a76[Pickpocket] Attempted to steal from " + victim.getName() + "!");
        }
    }
}
