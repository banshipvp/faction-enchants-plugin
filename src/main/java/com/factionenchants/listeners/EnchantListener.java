package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.abilities.armor.Hardened;
import com.factionenchants.enchantments.abilities.soul.DivineImmolation;
import com.factionenchants.enchantments.abilities.soul.Immortal;
import com.factionenchants.enchantments.abilities.soul.TungstenPlating;
import com.factionenchants.enchantments.abilities.tool.AutoSmelt;
import com.factionenchants.enchantments.abilities.tool.Detonate;
import com.factionenchants.enchantments.abilities.tool.Experience;
import com.factionenchants.enchantments.abilities.tool.Inquisitive;
import com.factionenchants.enchantments.abilities.tool.MasterInquisitive;
import com.factionenchants.enchantments.abilities.tool.ObsidianBreaker;
import com.factionenchants.enchantments.abilities.tool.Oxygenate;
import com.factionenchants.enchantments.abilities.tool.Reforged;
import com.factionenchants.enchantments.abilities.tool.Telepathy;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class EnchantListener implements Listener {

    private final FactionEnchantsPlugin plugin;

    public EnchantListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, this::tickPassiveEffects, 1L, 20L);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();
        Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(tool);

        boolean hasDetonate = false;
        boolean hasAutoSmelt = false;
        boolean hasTelepathy = false;
        int detonateLevel = 1;

        for (Map.Entry<CustomEnchantment, Integer> entry : enchants.entrySet()) {
            if (entry.getKey() instanceof Detonate) { hasDetonate = true; detonateLevel = entry.getValue(); }
            else if (entry.getKey() instanceof AutoSmelt) { hasAutoSmelt = true; }
            else if (entry.getKey() instanceof Telepathy) { hasTelepathy = true; }
            else if (entry.getKey() instanceof Oxygenate o && player.isInWater()) { o.tryRefill(player); }
            else if (entry.getKey() instanceof Experience e) { e.tryGiveExp(player, entry.getValue()); }
            else if (entry.getKey() instanceof Inquisitive i) { i.onBlockBreak(player, event, entry.getValue()); }
            else if (entry.getKey() instanceof MasterInquisitive mi) { mi.onBlockBreak(player, event, entry.getValue()); }
        }

        if (hasDetonate) {
            event.setCancelled(true);
            Block target = player.getTargetBlockExact(5);
            if (target != null && target.getType() != Material.AIR) {
                int radius = detonateLevel;
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        for (int dz = -radius; dz <= radius; dz++) {
                            Block b = target.getRelative(dx, dy, dz);
                            Material bType = b.getType();
                            if (bType == Material.AIR || bType == Material.BEDROCK) continue;
                            if (bType == Material.WATER || bType == Material.LAVA) { b.setType(Material.AIR); continue; }
                            if (hasAutoSmelt) {
                                Material smelt = AutoSmelt.getSmeltResult(bType);
                                if (smelt != null) { b.setType(Material.AIR); player.getInventory().addItem(new ItemStack(smelt)); continue; }
                                Collection<ItemStack> rawDrops = b.getDrops(tool);
                                boolean smelted = false;
                                for (ItemStack drop : rawDrops) {
                                    Material s = AutoSmelt.getSmeltResult(drop.getType());
                                    if (s != null) { b.setType(Material.AIR); player.getInventory().addItem(new ItemStack(s, drop.getAmount())); smelted = true; break; }
                                }
                                if (smelted) continue;
                            }
                            if (hasTelepathy) {
                                Collection<ItemStack> drops = b.getDrops(tool);
                                b.setType(Material.AIR);
                                drops.forEach(d -> player.getInventory().addItem(d.clone()));
                                continue;
                            }
                            if (player.getGameMode() == GameMode.CREATIVE) b.setType(Material.AIR);
                            else b.breakNaturally(tool);
                        }
                    }
                }
            }
            return;
        }

        if (hasAutoSmelt) {
            Material smeltResult = AutoSmelt.getSmeltResult(block.getType());
            if (smeltResult != null) {
                event.setDropItems(false);
                player.getInventory().addItem(new ItemStack(smeltResult, 1));
            } else {
                Collection<ItemStack> rawDrops = block.getDrops(tool);
                boolean anySmelted = false;
                for (ItemStack drop : rawDrops) {
                    Material smelted = AutoSmelt.getSmeltResult(drop.getType());
                    if (smelted != null) {
                        event.setDropItems(false);
                        player.getInventory().addItem(new ItemStack(smelted, drop.getAmount()));
                        anySmelted = true;
                    }
                }
                if (hasTelepathy && !anySmelted) {
                    event.setDropItems(false);
                    for (ItemStack drop : rawDrops) {
                        player.getInventory().addItem(drop.clone());
                    }
                }
            }
            return;
        }

        if (hasTelepathy) {
            Collection<ItemStack> drops = block.getDrops(tool);
            event.setDropItems(false);
            for (ItemStack drop : drops) {
                player.getInventory().addItem(drop.clone());
            }
        }
    }

    private static final Set<Material> OBSIDIAN_TYPES = Set.of(
            Material.OBSIDIAN, Material.CRYING_OBSIDIAN, Material.RESPAWN_ANCHOR
    );

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null || !OBSIDIAN_TYPES.contains(block.getType())) return;
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(tool);
        boolean hasObsidianBreaker = enchants.keySet().stream().anyMatch(e -> e instanceof ObsidianBreaker);
        if (!hasObsidianBreaker) return;
        event.setCancelled(true);
        if (player.getGameMode() == GameMode.CREATIVE) {
            block.setType(Material.AIR);
        } else {
            block.breakNaturally(tool);
        }
    }

    private void tickPassiveEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.CREATIVE
                    || player.getGameMode() == GameMode.SPECTATOR) continue;

            boolean hasOverload = false;
            boolean hasImplants = false;

            for (ItemStack armor : player.getInventory().getArmorContents()) {
                if (armor == null || armor.getType().isAir()) continue;

                for (Map.Entry<CustomEnchantment, Integer> e : plugin.getEnchantmentManager().getEnchantmentsOnItem(armor).entrySet()) {
                    String id = e.getKey().getId();
                    if ("overload".equals(id) || "godly_overload".equals(id)) {
                        hasOverload = true;
                    }
                    if ("implants".equals(id)) {
                        hasImplants = true;
                    }

                    int soulCostPerTick = e.getKey().getSoulCostPerTick();
                    if (soulCostPerTick > 0 && !plugin.getSoulManager().consumeSouls(player, soulCostPerTick)) continue;
                    e.getKey().onTickPassive(player, e.getValue(), armor);
                }
            }

            ItemStack held = player.getInventory().getItemInMainHand();
            for (Map.Entry<CustomEnchantment, Integer> e : plugin.getEnchantmentManager().getEnchantmentsOnItem(held).entrySet()) {
                if (e.getKey() instanceof DivineImmolation) continue;
                int soulCostPerTick = e.getKey().getSoulCostPerTick();
                if (soulCostPerTick > 0 && !plugin.getSoulManager().consumeSouls(player, soulCostPerTick)) continue;
                e.getKey().onTickPassive(player, e.getValue(), held);
            }

            if (!hasOverload && player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
                player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
            }
            if (!hasImplants && player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                player.removePotionEffect(PotionEffectType.REGENERATION);
            }
            if (!hasImplants && player.hasPotionEffect(PotionEffectType.SATURATION)) {
                player.removePotionEffect(PotionEffectType.SATURATION);
            }
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Map<CustomEnchantment, Integer> enchants = plugin.getEnchantmentManager().getEnchantmentsOnItem(item);
        for (Map.Entry<CustomEnchantment, Integer> e : enchants.entrySet()) {
            if (e.getKey() instanceof Hardened h && h.shouldCancelDamage(e.getValue())) {
                event.setCancelled(true);
                return;
            }
            if (e.getKey() instanceof Reforged r && r.shouldCancelDamage(e.getValue())) {
                event.setCancelled(true);
                return;
            }
            if (e.getKey() instanceof Immortal immortal && immortal.tryPrevent(player, e.getValue())) {
                event.setCancelled(true);
                return;
            }
            if (e.getKey() instanceof TungstenPlating tp && tp.tryPrevent(player, e.getValue())) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
