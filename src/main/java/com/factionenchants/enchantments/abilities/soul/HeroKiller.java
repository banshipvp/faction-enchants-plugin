package com.factionenchants.enchantments.abilities.soul;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class HeroKiller extends CustomEnchantment {

    public HeroKiller() {
        super("hero_killer", "Hero Killer", 3, EnchantTier.SOUL, ApplicableGear.AXE);
    }

    @Override
    public String getDescription() {
        return "Deal (level x 5)% more damage to players wearing Heroic Armor.";
    }

    @Override
    public int getSoulCostPerProc() {
        return 0; // no soul cost — passive damage bonus
    }

    @Override
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(target instanceof Player victim)) return;

        boolean wearingHeroic = false;
        for (ItemStack armor : victim.getInventory().getArmorContents()) {
            if (armor == null || !armor.hasItemMeta() || !armor.getItemMeta().hasLore()) continue;
            for (String line : armor.getItemMeta().getLore()) {
                String stripped = line.replaceAll("\u00a7[0-9a-fk-or]", "").trim();
                if (stripped.startsWith("Heroic")) { wearingHeroic = true; break; }
            }
            if (wearingHeroic) break;
        }

        if (wearingHeroic) {
            double bonus = level * 0.05;
            event.setDamage(event.getDamage() * (1.0 + bonus));
        }
    }
}
