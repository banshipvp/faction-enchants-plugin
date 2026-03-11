package com.factionenchants.listeners;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.items.FallenHeroItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

/**
 * Handles the Fallen Hero Bone item.
 *
 * Right-clicking a Fallen Hero Bone consumes it and spawns a powerful
 * WitherSkeleton named after the linked GKit. Killing it yields a 50% chance
 * to receive the corresponding GKit Gem via the gkitgem console command.
 */
public class FallenHeroListener implements Listener {

    private final FactionEnchantsPlugin plugin;
    private final NamespacedKey fallenHeroKey;
    private final NamespacedKey fallenHeroKitKey;
    private final Random random = new Random();

    public FallenHeroListener(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        this.fallenHeroKey = new NamespacedKey(plugin, "fallen_hero");
        this.fallenHeroKitKey = new NamespacedKey(plugin, "fallen_hero_kit");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        String kitName = FallenHeroItem.getKitName(plugin, item);
        if (kitName == null) return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        // Consume one bone
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }

        // Spawn WitherSkeleton slightly in front of the player
        Location spawnLoc = player.getLocation().add(player.getLocation().getDirection().multiply(2));
        spawnLoc.setY(player.getLocation().getY());

        WitherSkeleton mob = (WitherSkeleton) player.getWorld()
                .spawnEntity(spawnLoc, EntityType.WITHER_SKELETON);

        String displayName = "§c§l☠ " + FallenHeroItem.capitalize(kitName) + " Fallen Hero §c§l☠";
        mob.setCustomName(displayName);
        mob.setCustomNameVisible(true);
        mob.setRemoveWhenFarAway(true);

        // 100 HP (50 hearts)
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100.0);
        mob.setHealth(100.0);

        // Full Netherite armour with Prot V + Unbreaking III
        mob.getEquipment().setHelmet(armorPiece(Material.NETHERITE_HELMET));
        mob.getEquipment().setChestplate(armorPiece(Material.NETHERITE_CHESTPLATE));
        mob.getEquipment().setLeggings(armorPiece(Material.NETHERITE_LEGGINGS));
        mob.getEquipment().setBoots(armorPiece(Material.NETHERITE_BOOTS));
        mob.getEquipment().setItemInMainHand(sword());

        // No gear drops
        mob.getEquipment().setHelmetDropChance(0f);
        mob.getEquipment().setChestplateDropChance(0f);
        mob.getEquipment().setLeggingsDropChance(0f);
        mob.getEquipment().setBootsDropChance(0f);
        mob.getEquipment().setItemInMainHandDropChance(0f);

        // Tag for death handler
        mob.getPersistentDataContainer().set(fallenHeroKey, PersistentDataType.BYTE, (byte) 1);
        mob.getPersistentDataContainer().set(fallenHeroKitKey, PersistentDataType.STRING, kitName);

        player.sendMessage("§c§l☠ §eThe Fallen Hero of §c§l"
                + FallenHeroItem.capitalize(kitName) + " §ehas appeared!");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.getPersistentDataContainer().has(fallenHeroKey, PersistentDataType.BYTE)) return;

        String kitName = entity.getPersistentDataContainer()
                .get(fallenHeroKitKey, PersistentDataType.STRING);
        if (kitName == null) return;

        // No vanilla drops
        event.getDrops().clear();
        event.setDroppedExp(0);

        Player killer = entity.getKiller();
        if (killer == null) return;

        if (random.nextInt(100) < 50) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "gkitgem give " + killer.getName() + " " + kitName + " 1");
            killer.sendMessage("§a§l✦ §aThe Fallen Hero dropped a §b§l"
                    + FallenHeroItem.capitalize(kitName) + " Gem§a!");
        } else {
            killer.sendMessage("§7The Fallen Hero left nothing behind...");
        }
    }

    // --- helpers ---

    private ItemStack armorPiece(Material material) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(Enchantment.getByKey(NamespacedKey.minecraft("protection")), 5);
        item.addUnsafeEnchantment(Enchantment.getByKey(NamespacedKey.minecraft("unbreaking")), 3);
        return item;
    }

    private ItemStack sword() {
        ItemStack s = new ItemStack(Material.NETHERITE_SWORD);
        s.addUnsafeEnchantment(Enchantment.getByKey(NamespacedKey.minecraft("sharpness")), 5);
        s.addUnsafeEnchantment(Enchantment.getByKey(NamespacedKey.minecraft("unbreaking")), 3);
        return s;
    }
}
