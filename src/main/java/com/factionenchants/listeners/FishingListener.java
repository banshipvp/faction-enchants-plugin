package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.abilities.misc.DeepDiver;
import com.factionenchants.enchantments.abilities.misc.Dredger;
import com.factionenchants.enchantments.abilities.misc.Sizzle;
import com.factionenchants.enchantments.abilities.misc.TrophySeeker;
import com.factionenchants.enchantments.abilities.misc.QuickReeler;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

public class FishingListener implements Listener {

    private final FactionEnchantsPlugin plugin;
    private final Random random = new Random();

    public FishingListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack rod = player.getInventory().getItemInMainHand();
        if (!rod.getType().name().equals("FISHING_ROD")) {
            rod = player.getInventory().getItemInOffHand();
        }
        if (!rod.getType().name().equals("FISHING_ROD")) return;

        Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(rod);

        int dredgerLevel = 0;
        int quickReelerLevel = 0;
        int trophySeekerLevel = 0;
        int deepDiverLevel = 0;
        int sizzleLevel = 0;

        for (Map.Entry<CustomEnchantment, Integer> entry : enchants.entrySet()) {
            if (entry.getKey() instanceof Dredger) dredgerLevel = entry.getValue();
            if (entry.getKey() instanceof QuickReeler) quickReelerLevel = entry.getValue();
            if (entry.getKey() instanceof TrophySeeker) trophySeekerLevel = entry.getValue();
            if (entry.getKey() instanceof DeepDiver) deepDiverLevel = entry.getValue();
            if (entry.getKey() instanceof Sizzle) sizzleLevel = entry.getValue();
        }

        // Quick Reeler: reduce wait time when rod is cast
        if (quickReelerLevel > 0 && event.getState() == PlayerFishEvent.State.FISHING) {
            FishHook hook = event.getHook();
            double reduction = 1.0 - (quickReelerLevel * 0.05);
            reduction = Math.max(0.5, reduction);
            hook.setMinWaitTime((int) (hook.getMinWaitTime() * reduction));
            hook.setMaxWaitTime((int) (hook.getMaxWaitTime() * reduction));
        }

        // Dredger: bonus XP on catch
        if (dredgerLevel > 0 && event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            int bonusChance = 20 + dredgerLevel * 12;
            if (random.nextInt(100) < bonusChance) {
                event.setExpToDrop(event.getExpToDrop() * 2);
                player.sendMessage("\u00a7aDredger \u00a77\u00bb \u00a7f2x EXP!");
            }
        }

        // Trophy Seeker: increase fishing luck on catch
        if (trophySeekerLevel > 0 && event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.LUCK, 200, trophySeekerLevel - 1, true, false));
        }

        // Deep Diver: +5% treasure chance per level via LUCK effect on catch
        if (deepDiverLevel > 0 && event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.LUCK, 200, deepDiverLevel, true, false));
        }

        // Sizzle: chance to auto-cook caught fish
        if (sizzleLevel > 0 && event.getState() == PlayerFishEvent.State.CAUGHT_FISH
                && event.getCaught() instanceof Item caughtItem) {
            if (random.nextDouble() < Sizzle.getProcChance(sizzleLevel)) {
                org.bukkit.inventory.ItemStack fish = caughtItem.getItemStack();
                Material cooked = Sizzle.COOK_MAP.get(fish.getType());
                if (cooked != null) {
                    fish.setType(cooked);
                    caughtItem.setItemStack(fish);
                }
            }
        }
    }
}
