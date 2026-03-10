package com.factionenchants.enchantments;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.abilities.armor.CreeperArmor;
import com.factionenchants.enchantments.abilities.armor.Dodge;
import com.factionenchants.enchantments.abilities.armor.Hardened;
import com.factionenchants.enchantments.abilities.armor.IceAspect;
import com.factionenchants.enchantments.abilities.armor.Aegis;
import com.factionenchants.enchantments.abilities.armor.Armored;
import com.factionenchants.enchantments.abilities.armor.BloodLink;
import com.factionenchants.enchantments.abilities.armor.BloodLust;
import com.factionenchants.enchantments.abilities.armor.Clarity;
import com.factionenchants.enchantments.abilities.armor.DeathGod;
import com.factionenchants.enchantments.abilities.armor.Deathbringer;
import com.factionenchants.enchantments.abilities.armor.Destruction;
import com.factionenchants.enchantments.abilities.armor.Diminish;
import com.factionenchants.enchantments.abilities.armor.Drunk;
import com.factionenchants.enchantments.abilities.armor.EnchantReflect;
import com.factionenchants.enchantments.abilities.armor.Enlighted;
import com.factionenchants.enchantments.abilities.armor.Exterminator;
import com.factionenchants.enchantments.abilities.armor.Gears;
import com.factionenchants.enchantments.abilities.armor.EnderWalker;
import com.factionenchants.enchantments.abilities.armor.Ghost;
import com.factionenchants.enchantments.abilities.armor.Guardians;
import com.factionenchants.enchantments.abilities.armor.Heavy;
import com.factionenchants.enchantments.abilities.armor.Implants;
import com.factionenchants.enchantments.abilities.armor.Lucky;
import com.factionenchants.enchantments.abilities.armor.Marksman;
import com.factionenchants.enchantments.abilities.armor.Metaphysical;
import com.factionenchants.enchantments.abilities.armor.Obsidianshield;
import com.factionenchants.enchantments.abilities.armor.Ragdoll;
import com.factionenchants.enchantments.abilities.armor.Valor;
import com.factionenchants.enchantments.abilities.bow.DimensionRift;
import com.factionenchants.enchantments.abilities.bow.EagleEye;
import com.factionenchants.enchantments.abilities.bow.Hellfire;
import com.factionenchants.enchantments.abilities.bow.Longbow;
import com.factionenchants.enchantments.abilities.bow.Pacify;
import com.factionenchants.enchantments.abilities.bow.Piercing;
import com.factionenchants.enchantments.abilities.bow.Unfocus;
import com.factionenchants.enchantments.abilities.sword.AntiGank;
import com.factionenchants.enchantments.abilities.sword.Bleed;
import com.factionenchants.enchantments.abilities.sword.Devour;
import com.factionenchants.enchantments.abilities.sword.Disarmor;
import com.factionenchants.enchantments.abilities.sword.DoubleStrike;
import com.factionenchants.enchantments.abilities.soul.DivineImmolation;
import com.factionenchants.enchantments.abilities.soul.HeroKiller;
import com.factionenchants.enchantments.abilities.soul.Immortal;
import com.factionenchants.enchantments.abilities.soul.Inertia;
import com.factionenchants.enchantments.abilities.soul.NatureWrath;
import com.factionenchants.enchantments.abilities.soul.Paradox;
import com.factionenchants.enchantments.abilities.soul.Phoenix;
import com.factionenchants.enchantments.abilities.soul.Rogue;
import com.factionenchants.enchantments.abilities.soul.Sabotage;
import com.factionenchants.enchantments.abilities.soul.SoulTrap;
import com.factionenchants.enchantments.abilities.soul.Teleblock;
import com.factionenchants.enchantments.abilities.soul.TungstenPlating;
import com.factionenchants.enchantments.abilities.sword.Enrage;
import com.factionenchants.enchantments.abilities.tool.AutoSmelt;
import com.factionenchants.enchantments.abilities.tool.Blacksmith;
import com.factionenchants.enchantments.abilities.tool.Detonate;
import com.factionenchants.enchantments.abilities.tool.Experience;
import com.factionenchants.enchantments.abilities.tool.Fuse;
import com.factionenchants.enchantments.abilities.tool.Inquisitive;
import com.factionenchants.enchantments.abilities.tool.MasterInquisitive;
import com.factionenchants.enchantments.abilities.tool.ObsidianBreaker;
import com.factionenchants.enchantments.abilities.tool.Oxygenate;
import com.factionenchants.enchantments.abilities.tool.QuickReeler;
import com.factionenchants.enchantments.abilities.tool.Reforged;
import com.factionenchants.enchantments.abilities.tool.Telepathy;
import com.factionenchants.enchantments.abilities.sword.Barbarian;
import com.factionenchants.enchantments.abilities.sword.Block;
import com.factionenchants.enchantments.abilities.sword.BossSlayer;
import com.factionenchants.enchantments.abilities.sword.Cleave;
import com.factionenchants.enchantments.abilities.sword.Corrupt;
import com.factionenchants.enchantments.abilities.sword.Disintegrate;
import com.factionenchants.enchantments.abilities.sword.Dominate;
import com.factionenchants.enchantments.abilities.armor.Angelic;
import com.factionenchants.enchantments.abilities.armor.ArrowDeflect;
import com.factionenchants.enchantments.abilities.armor.AvengingAngel;
import com.factionenchants.enchantments.abilities.armor.Sticky;
import com.factionenchants.enchantments.abilities.armor.Tank;
import com.factionenchants.enchantments.abilities.axe.ArrowBreak;
import com.factionenchants.enchantments.abilities.axe.Blessed;
import com.factionenchants.enchantments.abilities.bow.ArrowLifesteal;
import com.factionenchants.enchantments.abilities.misc.DeepDiver;
import com.factionenchants.enchantments.abilities.misc.Dredger;
import com.factionenchants.enchantments.abilities.misc.TrophySeeker;
import com.factionenchants.enchantments.abilities.sword.Assassin;
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
        register(new IceAspect());
        register(new Block());
        register(new Disintegrate());
        register(new Cleave());
        register(new Corrupt());
        register(new Dominate());
        register(new CreeperArmor());
        register(new Dodge());
        register(new Detonate());
        register(new DimensionRift());
        register(new EagleEye());
        register(new EnderWalker());
        register(new Enrage());
        register(new Fuse());
        register(new Ghost());
        register(new Guardians());
        register(new Heavy());
        register(new Hellfire());
        register(new Implants());
        register(new Longbow());
        register(new Lucky());
        register(new Marksman());
        register(new Metaphysical());
        register(new Obsidianshield());
        register(new Pacify());
        register(new Piercing());
        register(new Ragdoll());
        register(new Unfocus());
        register(new Valor());
        register(new Bleed());
        register(new AutoSmelt());
        register(new DivineImmolation());
        register(new Experience());
        register(new Inquisitive());
        register(new MasterInquisitive());
        register(new ObsidianBreaker());
        register(new Oxygenate());
        register(new QuickReeler());
        register(new Reforged());
        register(new Telepathy());
        register(new Dredger());
        register(new TrophySeeker());
        register(new Hardened());

        // Soul tier
        register(new HeroKiller());
        register(new Immortal());
        register(new Inertia());
        register(new NatureWrath());
        register(new Paradox());
        register(new Phoenix());
        register(new Rogue());
        register(new Sabotage());
        register(new SoulTrap());
        register(new Teleblock());
        register(new TungstenPlating());

        // New enchantments
        register(new Sticky());
        register(new Tank());
        register(new ArrowDeflect());
        register(new Angelic());
        register(new ArrowBreak());
        register(new ArrowLifesteal());
        register(new Assassin());
        register(new AvengingAngel());
        register(new Blessed());

        // Legendary tier
        register(new Aegis());
        register(new AntiGank());
        register(new Armored());
        register(new Barbarian());
        register(new Blacksmith());
        register(new BloodLink());
        register(new BloodLust());
        register(new BossSlayer());
        register(new Clarity());
        register(new DeathGod());
        register(new Deathbringer());
        register(new DeepDiver());
        register(new Destruction());
        register(new Devour());
        register(new Diminish());
        register(new Disarmor());
        register(new DoubleStrike());
        register(new Drunk());
        register(new EnchantReflect());
        register(new Enlighted());
        register(new Exterminator());
        register(new Gears());

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