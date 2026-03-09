package com.factionenchants.enchantments;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.enchantments.abilities.armor.*;
import com.factionenchants.enchantments.abilities.sword.*;
import com.factionenchants.enchantments.abilities.sword.ExtendedLooting;
import com.factionenchants.enchantments.abilities.tool.*;
import com.factionenchants.enchantments.abilities.bow.*;
import com.factionenchants.enchantments.abilities.misc.*;
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
        // Existing enchants
        register(new Detonate());
        register(new AutoSmelt());
        register(new Telepathy());
        register(new ObsidianBreaker());
        register(new TokenBoost());
        register(new DivineImmolation());
        register(new Vampiric());
        register(new Decapitate());
        register(new Confusion());
        register(new Thundering());
        register(new Incinerate());
        register(new PoisonBlade());
        register(new Cripple());
        register(new Drunk());
        register(new Tanky());
        register(new Immunity());
        register(new Overload());
        register(new Adrenaline());
        register(new IceAspect());
        register(new Gears());
        register(new Molten());
        register(new Springs());
        register(new Volley());
        register(new ExplosiveBow());
        register(new Snipe());
        register(new Paralyze());

        // Simple tier
        register(new Aquatic());
        register(new Glowing());
        register(new Shuffle());
        register(new Headless());
        register(new Obliterate());
        register(new Epicness());
        register(new Etheral());
        register(new Insomnia());
        register(new Lightning());
        register(new Oxygenate());
        register(new Experience());
        register(new Haste());
        register(new Strike());
        register(new Impact());
        register(new Twinge());

        // Unique tier
        register(new Ward());
        register(new Curse());
        register(new EnderShift());
        register(new Sustain());
        register(new SelfDestruct());
        register(new Commander());
        register(new PlagueCarrier());
        register(new Lifebloom());
        register(new Featherweight());
        register(new SkillSwipe());
        register(new Famine());
        register(new Berserk());
        register(new Ravenous());
        register(new Virus());

        // Elite tier
        register(new AntiGravity());
        register(new EnderSlayer());
        register(new NetherSlayer());
        register(new Reaper());
        register(new Blind());
        register(new Execute());
        register(new Cactus());
        register(new Frozen());
        register(new Poisoned());
        register(new Stormcaller());
        register(new Voodoo());
        register(new Wither());
        register(new SmokeBomb());
        register(new Shockwave());
        register(new Trickster());
        register(new RocketEscape());
        register(new Trap());
        register(new Demonforged());
        register(new Greatsword());
        register(new Pummel());
        register(new Hardened());
        register(new Reforged());
        register(new UndeadRuse());
        register(new Hijack());
        register(new Snare());
        register(new Venom());
        register(new Infernal());
        register(new Farcast());
        register(new ExtendedLooting());

        // Ultimate tier
        register(new ArrowLifesteal());
        register(new ArrowDeflect());
        register(new Assassin());
        register(new Blessed());
        register(new Corrupt());
        register(new Ragdoll());
        register(new Block());
        register(new Dodge());
        register(new Enrage());
        register(new Guardians());
        register(new Aegis());
        register(new BloodLink());
        register(new BloodLust());
        register(new Implants());
        register(new Obsidianshield());
        register(new Demonic());
        register(new Disappear());
        register(new CreeperArmor());
        register(new Spirits());
        register(new Heavy());
        register(new Tank());
        register(new Valor());
        register(new Reinforced());
        register(new Marksman());
        register(new Piercing());
        register(new Unfocus());
        register(new Longbow());
        register(new EagleEye());
        register(new Sniper());
        register(new Hellfire());
        register(new Pacify());
        register(new ArrowBreak());
        register(new EnderWalker());
        register(new Metaphysical());
        register(new Pickpocket());
        register(new Distance());
        register(new Restore());
        register(new Disintegrate());
        register(new Dominate());
        register(new Arsonist());
        register(new Annihilate());
        register(new Bleed());

        // Legendary tier
        register(new KillAura());
        register(new LavaWalker());
        register(new Impale());
        register(new Protection());
        register(new Torrent());
        register(new Judgement());
        register(new Surprise());
        register(new Stun());
        register(new Unholy());
        register(new Quiver());
        register(new Fat());
        register(new Hex());
        register(new Barbarian());
        register(new AntiGank());
        register(new Clarity());
        register(new Deathbringer());
        register(new DoubleStrike());
        register(new Enlighted());
        register(new Inquisitive());
        register(new Inversion());
        register(new Lifesteal());
        register(new Rage());
        register(new Silence());
        register(new Deepwounds());
        register(new Armored());
        register(new Exterminator());
        register(new Blacksmith());
        register(new Abiding());
        register(new Devour());
        register(new Diminish());
        register(new Disarmor());
        register(new DeathGod());
        register(new Insanity());
        register(new Destruction());

        // Soul tier
        register(new Rogue());
        register(new Sabotage());

        // Heroic tier
        register(new PolymorphicMetaphysical());
        register(new Soulbound());
        register(new ReinforcedTank());
        register(new EpidemicCarrier());
        register(new GodlyOverload());
        register(new ReflectiveBlock());
        register(new MasterInquisitive());
        register(new PlanetaryDeathbringer());
        register(new DivineEnlighted());
        register(new LethalSniper());

        // Mastery tier
        register(new SoulGrind());
        register(new Neutralize());
        register(new Halloweenify());
        register(new MarkOfTheBeast());
        register(new Horrify());
        register(new ChainLifesteal());
        register(new Poltergeist());
        register(new FeignDeath());
        register(new SoulSiphon());
        register(new DeathPact());
        register(new WebWalker());

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
