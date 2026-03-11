package com.factionenchants.enchantments.abilities.weapon;

import com.factionenchants.enchantments.CustomEnchantment;
import com.gmail.nossr50.api.ExperienceAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Training X — Weapon enchantment, UNIQUE tier.
 * Increases mcMMO XP gained in all COMBAT skills while equipped.
 * Bonus: level * 5 raw XP per kill across all combat skills.
 */
public class Training extends CustomEnchantment {

    private static final String[] COMBAT_SKILLS = {
            "SWORDS", "AXES", "ARCHERY", "UNARMED", "TAMING"
    };

    public Training() {
        super("training", "Training", 10, EnchantTier.UNIQUE, ApplicableGear.WEAPON);
    }

    @Override
    public String getDescription() {
        return "Increases mcMMO XP gained in all combat skills while equipped.";
    }

    @Override
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {
        if (!Bukkit.getPluginManager().isPluginEnabled("mcMMO")) return;
        int bonus = level * 5; // 5-50 bonus XP per kill per combat skill
        try {
            for (String skill : COMBAT_SKILLS) {
                ExperienceAPI.addRawXP(killer, skill, bonus);
            }
        } catch (Exception ignored) {}
    }
}
