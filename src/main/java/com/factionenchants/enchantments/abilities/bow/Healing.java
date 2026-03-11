package com.factionenchants.enchantments.abilities.bow;

import com.factionenchants.enchantments.CustomEnchantment;
import local.simplefactions.FactionManager;
import local.simplefactions.SimpleFactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Healing IV — Bow enchantment, UNIQUE tier.
 * Heals friendly players hit with an arrow shot by this bow.
 * Also has a chance to repair their armor and grant absorption / health boost.
 */
public class Healing extends CustomEnchantment {

    private static final Random random = new Random();

    public Healing() {
        super("healing", "Healing", 4, EnchantTier.UNIQUE, ApplicableGear.BOW);
    }

    @Override
    public String getDescription() {
        return "Heals friendly players hit with arrow shot by this bow. Also has a chance to increase durability of armor and give absorption/health boost.";
    }

    @Override
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player targetPlayer)) return;
        if (!isFriendly(shooter, targetPlayer)) return;

        // Cancel the arrow damage and heal instead
        event.setCancelled(true);

        // Apply Regeneration (scales with level)
        targetPlayer.addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION, 20 * level * 3, level - 1, false, true));

        // Chance (20% per level) to also repair armor and grant bonus effects
        if (random.nextInt(100) < level * 20) {
            repairArmor(targetPlayer, level);
            targetPlayer.addPotionEffect(new PotionEffect(
                    PotionEffectType.ABSORPTION, 20 * 10, level - 1, false, true));
            targetPlayer.addPotionEffect(new PotionEffect(
                    PotionEffectType.HEALTH_BOOST, 20 * 10, level - 1, false, true));
        }
    }

    /** Repairs each worn armor piece by roughly 10% of its max durability per level. */
    private void repairArmor(Player player, int level) {
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType().isAir()) continue;
            ItemMeta meta = armor.getItemMeta();
            if (!(meta instanceof Damageable dmg)) continue;
            int maxDurability = armor.getType().getMaxDurability();
            int repairAmount = (int) (maxDurability * 0.10 * level);
            int newDamage = Math.max(0, dmg.getDamage() - repairAmount);
            dmg.setDamage(newDamage);
            armor.setItemMeta(dmg);
        }
        // Refresh inventory so clients see the updated durability
        player.updateInventory();
    }

    /** Returns true if target is in the same faction or an allied faction as shooter. */
    private boolean isFriendly(Player shooter, Player target) {
        Plugin sfPlugin = Bukkit.getPluginManager().getPlugin("SimpleFactions");
        if (!(sfPlugin instanceof SimpleFactionsPlugin sfp)) return false;

        FactionManager fm = sfp.getFactionManager();
        FactionManager.Faction shooterFaction = fm.getFaction(shooter.getUniqueId());
        FactionManager.Faction targetFaction  = fm.getFaction(target.getUniqueId());

        if (shooterFaction == null || targetFaction == null) return false;

        // Same faction
        if (shooterFaction.getName().equalsIgnoreCase(targetFaction.getName())) return true;

        // Allied factions
        return shooterFaction.isAlly(targetFaction.getName());
    }
}
