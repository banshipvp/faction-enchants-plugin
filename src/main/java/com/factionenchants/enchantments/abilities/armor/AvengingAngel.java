package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Avenging Angel — Armor enchantment, ULTIMATE tier, max level IV.
 * When an ally (any nearby player) dies within 100 blocks:
 *   - The wearer is healed to full HP.
 *   - The wearer receives absorption hearts for up to 10 seconds (2.5s × level).
 *
 * Triggered externally by {@link com.factionenchants.listeners.AvengingAngelListener}.
 */
public class AvengingAngel extends CustomEnchantment {

    /** Detection radius (blocks). */
    public static final double TRIGGER_RADIUS = 100.0;

    public AvengingAngel() {
        super("avenging_angel", "Avenging Angel", 4, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Heal to full HP when an ally dies within up to 100 blocks of your location " +
               "and receive absorption hearts for up to 10 seconds (based on level).";
    }

    /**
     * Called by AvengingAngelListener when a nearby player dies.
     *
     * @param beneficiary the player wearing Avenging Angel armor
     * @param level       the enchantment level (1–4)
     * @param plugin      the plugin instance for scheduling
     */
    public void triggerEffect(Player beneficiary, int level, Plugin plugin) {
        // Heal to full HP
        double maxHp = beneficiary.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        beneficiary.setHealth(maxHp);

        // Absorption based on level: level I → 4♥ for 2.5s, level IV → 16♥ for 10s
        int absorptionAmplifier = level - 1; // 0→3 (Absorption I–IV)
        int durationTicks = level * 50;      // 2.5s × level (50 ticks = 2.5s)

        beneficiary.addPotionEffect(
                new PotionEffect(PotionEffectType.ABSORPTION, durationTicks, absorptionAmplifier, false, true),
                true
        );

        beneficiary.sendMessage("§6✦ §eAvenging Angel §6✦ §fAn ally has fallen! You have been healed and received absorption.");
    }
}
