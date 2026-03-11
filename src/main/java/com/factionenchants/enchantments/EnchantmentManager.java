package com.factionenchants.enchantments;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.abilities.armor.AntiGravity;
import com.factionenchants.enchantments.abilities.armor.Cactus;
import com.factionenchants.enchantments.abilities.armor.Hardened;
import com.factionenchants.enchantments.abilities.armor.Lifebloom;
import com.factionenchants.enchantments.abilities.armor.Nimble;
import com.factionenchants.enchantments.abilities.armor.PlagueCarrier;
import com.factionenchants.enchantments.abilities.armor.Poisoned;
import com.factionenchants.enchantments.abilities.armor.RepairGuard;
import com.factionenchants.enchantments.abilities.armor.Resilience;
import com.factionenchants.enchantments.abilities.armor.RocketEscape;
import com.factionenchants.enchantments.abilities.armor.SelfDestruct;
import com.factionenchants.enchantments.abilities.armor.Shockwave;
import com.factionenchants.enchantments.abilities.armor.SmokeBomb;
import com.factionenchants.enchantments.abilities.armor.SpiritLink;
import com.factionenchants.enchantments.abilities.armor.Springs;
import com.factionenchants.enchantments.abilities.armor.Stormcaller;
import com.factionenchants.enchantments.abilities.armor.Trickster;
import com.factionenchants.enchantments.abilities.armor.UndeadRuse;
import com.factionenchants.enchantments.abilities.armor.Voodoo;
import com.factionenchants.enchantments.abilities.armor.WitherEnchant;
import com.factionenchants.enchantments.abilities.axe.Funnel;
import com.factionenchants.enchantments.abilities.bow.Farcast;
import com.factionenchants.enchantments.abilities.bow.Healing;
import com.factionenchants.enchantments.abilities.bow.Hijack;
import com.factionenchants.enchantments.abilities.bow.Lightning;
import com.factionenchants.enchantments.abilities.bow.TargetTracking;
import com.factionenchants.enchantments.abilities.bow.Teleportation;
import com.factionenchants.enchantments.abilities.bow.Venom;
import com.factionenchants.enchantments.abilities.bow.Virus;
import com.factionenchants.enchantments.abilities.misc.QuickReeler;
import com.factionenchants.enchantments.abilities.sword.Blind;
import com.factionenchants.enchantments.abilities.sword.Denonforged;
import com.factionenchants.enchantments.abilities.sword.Execute;
import com.factionenchants.enchantments.abilities.sword.Greatsword;
import com.factionenchants.enchantments.abilities.sword.Insomnia;
import com.factionenchants.enchantments.abilities.sword.Paralyze;
import com.factionenchants.enchantments.abilities.sword.Poison;
import com.factionenchants.enchantments.abilities.sword.Reforged;
import com.factionenchants.enchantments.abilities.sword.SkillSwipe;
import com.factionenchants.enchantments.abilities.sword.Solitude;
import com.factionenchants.enchantments.abilities.sword.ThunderingBlow;
import com.factionenchants.enchantments.abilities.sword.Trap;
import com.factionenchants.enchantments.abilities.sword.Vampire;
import com.factionenchants.enchantments.abilities.tool.ObsidianDestroyer;
import com.factionenchants.enchantments.abilities.tool.Skilling;
import com.factionenchants.enchantments.abilities.tool.Telepathy;
import com.factionenchants.enchantments.abilities.weapon.Obliterate;
import com.factionenchants.enchantments.abilities.weapon.Shackle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchantmentManager {

    private final FactionEnchantsPlugin plugin;
    private final Map<String, CustomEnchantment> enchantments = new HashMap<>();

    public EnchantmentManager(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerEnchantments() {
        // Unique tier enchantments

        // Armor enchantments
        register(new SelfDestruct());
        register(new PlagueCarrier());
        register(new Nimble());
        register(new Lifebloom());

        // Sword enchantments
        register(new SkillSwipe());

        // Bow enchantments
        register(new Virus());

        // Tool enchantments
        register(new Telepathy());
        register(new Skilling());
        register(new ObsidianDestroyer());

        // Elite tier enchantments

        // Armor / Boots / Helmet enchantments
        register(new AntiGravity());
        register(new Cactus());
        register(new Hardened());
        register(new Poisoned());
        register(new RepairGuard());
        register(new Resilience());
        register(new RocketEscape());
        register(new Shockwave());
        register(new SmokeBomb());
        register(new SpiritLink());
        register(new Springs());
        register(new Stormcaller());
        register(new Trickster());
        register(new UndeadRuse());
        register(new Voodoo());
        register(new WitherEnchant());

        // Axe enchantments
        register(new Funnel());

        // Bow enchantments
        register(new Farcast());
        register(new Healing());
        register(new Hijack());
        register(new Lightning());
        register(new TargetTracking());
        register(new Teleportation());
        register(new Venom());

        // Fishing Rod enchantments
        register(new QuickReeler());

        // Sword enchantments
        register(new Blind());
        register(new Denonforged());
        register(new Execute());
        register(new Greatsword());
        register(new Insomnia());
        register(new Paralyze());
        register(new Poison());
        register(new Reforged());
        register(new Solitude());
        register(new ThunderingBlow());
        register(new Trap());
        register(new Vampire());

        // Weapon enchantments
        register(new Obliterate());
        register(new Shackle());

        plugin.getLogger().info("Registered " + enchantments.size() + " custom enchantments.");
    }

    private void register(CustomEnchantment enchantment) {
        enchantments.put(enchantment.getId(), enchantment);
    }

    public CustomEnchantment getEnchantment(String id) { return enchantments.get(id); }
    public Collection<CustomEnchantment> getAllEnchantments() { return enchantments.values(); }

    public List<CustomEnchantment> getEnchantmentsByTier(CustomEnchantment.EnchantTier tier) {
        List<CustomEnchantment> result = new ArrayList<>();
        for (CustomEnchantment e : enchantments.values()) {
            if (e.getTier() == tier) result.add(e);
        }
        return result;
    }

    public Map<CustomEnchantment, Integer> getEnchantmentsOnItem(ItemStack item) {
        Map<CustomEnchantment, Integer> result = new HashMap<>();
        if (item == null || !item.hasItemMeta()) return result;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return result;
        for (String line : meta.getLore()) {
            String stripped = line.replaceAll("\u00a7[0-9a-fk-or]", "").trim();
            for (CustomEnchantment enchant : enchantments.values()) {
                String name = enchant.getDisplayName().replaceAll("\u00a7[0-9a-fk-or]", "").trim();
                for (int lvl = 1; lvl <= enchant.getMaxLevel(); lvl++) {
                    if (stripped.equals(name + " " + toRoman(lvl)) || stripped.equals(name + " " + lvl)) {
                        result.put(enchant, lvl);
                    }
                }
            }
        }
        return result;
    }

    public ItemStack applyEnchantment(ItemStack item, CustomEnchantment enchant, int level) {
        if (!enchant.canApplyTo(item)) return item;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        String loreEntry = "\u00a7" + enchant.getTier().getColor() + enchant.getDisplayName() + " " + toRoman(level);
        lore.removeIf(l -> l.replaceAll("\u00a7[0-9a-fk-or]", "").trim().startsWith(enchant.getDisplayName().replaceAll("\u00a7[0-9a-fk-or]", "").trim()));
        lore.add(loreEntry);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack removeEnchantment(ItemStack item, CustomEnchantment enchant) {
        if (item == null || enchant == null || !item.hasItemMeta()) return item;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return item;

        String enchantName = enchant.getDisplayName().replaceAll("\u00a7[0-9a-fk-or]", "").trim();
        List<String> lore = new ArrayList<>(meta.getLore());
        lore.removeIf(line -> {
            String stripped = line.replaceAll("\u00a7[0-9a-fk-or]", "").trim();
            return stripped.startsWith(enchantName + " ");
        });

        if (lore.isEmpty()) {
            meta.setLore(null);
        } else {
            meta.setLore(lore);
        }

        item.setItemMeta(meta);
        return item;
    }

    public static String toRoman(int number) {
        return switch (number) {
            case 1 -> "I"; case 2 -> "II"; case 3 -> "III"; case 4 -> "IV";
            case 5 -> "V"; case 6 -> "VI"; case 7 -> "VII"; case 8 -> "VIII";
            case 9 -> "IX"; case 10 -> "X";
            default -> String.valueOf(number);
        };
    }
}
