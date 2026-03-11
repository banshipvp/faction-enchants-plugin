package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import local.simplefactions.FactionManager;
import local.simplefactions.SimpleFactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Teleportation V — Bow enchantment, ELITE tier.
 * When an ally, truce or faction member is hit by your arrow, you teleport to them.
 */
public class Teleportation extends CustomEnchantment {

    public Teleportation() {
        super("teleportation", "Teleportation", 5, EnchantTier.ELITE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "When an ally, truce or faction member is hit you teleport to them.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player targetPlayer)) return;
        if (!isAllyOrMember(shooter, targetPlayer)) return;

        // Cancel the damage — you are not hurting your ally
        event.setCancelled(true);
        shooter.teleport(targetPlayer.getLocation());
        shooter.sendMessage("§b[Teleportation] §fTeleported to " + targetPlayer.getName() + "!");
    }

    private boolean isAllyOrMember(Player shooter, Player target) {
        try {
            if (!Bukkit.getPluginManager().isPluginEnabled("SimpleFactions")) return false;
            SimpleFactionsPlugin sfp = SimpleFactionsPlugin.getInstance();
            if (sfp == null) return false;
            FactionManager fm = sfp.getFactionManager();
            FactionManager.Faction f1 = fm.getFactionByPlayer(shooter);
            FactionManager.Faction f2 = fm.getFactionByPlayer(target);
            if (f1 == null || f2 == null) return false;
            if (f1.getName().equalsIgnoreCase(f2.getName())) return true;
            if (f1.isAlly(f2.getName())) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
