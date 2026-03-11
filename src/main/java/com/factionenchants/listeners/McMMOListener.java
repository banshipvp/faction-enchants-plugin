package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

/**
 * Handles mcMMO XP gain events for the Skilling and Nimble enchantments.
 * Only registered when mcMMO is loaded on the server.
 *
 * Skilling boosts XP for gathering skills (+10% per level) based on held tool.
 * Nimble boosts Acrobatics XP (+15% per level) based on worn boots.
 */
public class McMMOListener implements Listener {

    private static final Set<PrimarySkillType> GATHERING_SKILLS = Set.of(
            PrimarySkillType.MINING,
            PrimarySkillType.WOODCUTTING,
            PrimarySkillType.HERBALISM,
            PrimarySkillType.EXCAVATION,
            PrimarySkillType.FISHING
    );

    private final FactionEnchantsPlugin plugin;

    public McMMOListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMcMMOXpGain(McMMOPlayerXpGainEvent event) {
        Player player = event.getPlayer();
        PrimarySkillType skill = event.getSkill();

        // Skilling — boost gathering skill XP based on held tool
        if (GATHERING_SKILLS.contains(skill)) {
            ItemStack held = player.getInventory().getItemInMainHand();
            for (Map.Entry<CustomEnchantment, Integer> e :
                    plugin.getEnchantmentManager().getEnchantmentsOnItem(held).entrySet()) {
                if (e.getKey().getId().equals("skilling")) {
                    int level = e.getValue();
                    event.setRawXpGained(event.getRawXpGained() * (1.0f + level * 0.10f));
                }
            }
        }

        // Nimble — boost Acrobatics XP based on worn boots
        if (skill == PrimarySkillType.ACROBATICS) {
            ItemStack boots = player.getInventory().getBoots();
            if (boots != null && !boots.getType().isAir()) {
                for (Map.Entry<CustomEnchantment, Integer> e :
                        plugin.getEnchantmentManager().getEnchantmentsOnItem(boots).entrySet()) {
                    if (e.getKey().getId().equals("nimble")) {
                        int level = e.getValue();
                        event.setRawXpGained(event.getRawXpGained() * (1.0f + level * 0.15f));
                    }
                }
            }
        }
    }
}
