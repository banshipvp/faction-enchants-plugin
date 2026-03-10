package com.factionenchants.commands;

import com.factionenchants.FactionEnchantsPlugin;
import com.factionenchants.books.DustManager;
import com.factionenchants.books.EnchantBook;
import com.factionenchants.enchantments.CustomEnchantment;
import com.factionenchants.enchantments.CustomEnchantment.EnchantTier;
import com.factionenchants.items.EnchantmentOrbItem;
import com.factionenchants.items.GodlyTransmogScrollItem;
import com.factionenchants.items.HeroicBlackScrollItem;
import com.factionenchants.items.HolyWhiteScrollItem;
import com.factionenchants.items.NameTagItem;
import com.factionenchants.items.RandomizationScrollItem;
import com.factionenchants.items.SoulGemItem;
import com.factionenchants.items.TransmogScrollItem;
import com.factionenchants.items.VanillaBlackScrollItem;
import com.factionenchants.items.WhiteScrollItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * /fegiveitem — Admin command for giving FactionEnchants custom items.
 *
 * Sub-commands:
 *   dust         <player> <TIER> <amount>         — give mystery dust
 *   xp           <player> <amount>                — give a Stored XP Bottle
 *   blackscroll  <player> [amount]                — give Black Scroll(s)
 *   rerollscroll <player> [amount]                — give Reroll Scroll(s)
 *   randombook   <player> <TIER> [amount]         — give random enchant book(s)
 */
public class GiveItemsCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBS = List.of(
            "dust", "duststack", "xp",
            "blackscroll", "heroicblackscroll", "vanillablackscroll",
            "rerollscroll", "randombook", "nametag",
            "whitescroll", "holywhitescroll",
            "transmogscroll", "godlytransmogscroll",
            "ultimaterandomizationscroll", "legendaryrandomizationscroll", "godlyrandomizationscroll",
            "soulgem", "soulgem_generator",
            "weaponorb", "armororb");

    private static final List<String> TIERS = List.of(
            "SIMPLE", "UNIQUE", "ELITE", "ULTIMATE", "LEGENDARY", "SOUL", "HEROIC", "MASTERY");

    private final FactionEnchantsPlugin plugin;
    private final Random random = new Random();

    public GiveItemsCommand(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("factionenchants.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }
        if (args.length < 2) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer §e" + args[1] + " §cis not online.");
            return true;
        }

        return switch (sub) {
            case "dust"                        -> handleDust(sender, target, args);
            case "duststack"                   -> handleDustStack(sender, target, args);
            case "xp"                          -> handleXp(sender, target, args);
            case "blackscroll"                 -> handleBlackScroll(sender, target, args);
            case "heroicblackscroll"           -> handleHeroicBlackScroll(sender, target, args);
            case "vanillablackscroll"          -> handleVanillaBlackScroll(sender, target, args);
            case "rerollscroll"                -> handleRerollScroll(sender, target, args);
            case "randombook"                  -> handleRandomBook(sender, target, args);
            case "nametag"                     -> handleNameTag(sender, target, args);
            case "whitescroll"                 -> handleWhiteScroll(sender, target, args);
            case "holywhitescroll"             -> handleHolyWhiteScroll(sender, target, args);
            case "transmogscroll"              -> handleTransmogScroll(sender, target, args);
            case "godlytransmogscroll"         -> handleGodlyTransmogScroll(sender, target, args);
            case "ultimaterandomizationscroll" -> handleUltimateRandomizationScroll(sender, target, args);
            case "legendaryrandomizationscroll"-> handleLegendaryRandomizationScroll(sender, target, args);
            case "godlyrandomizationscroll"    -> handleGodlyRandomizationScroll(sender, target, args);
            case "soulgem"                     -> handleSoulGem(sender, target, args);
            case "soulgem_generator"           -> handleSoulGemGenerator(sender, target, args);
            case "weaponorb"                   -> handleWeaponOrb(sender, target, args);
            case "armororb"                    -> handleArmorOrb(sender, target, args);
            default                            -> { sendHelp(sender); yield true; }
        };
    }

    // /fegiveitem dust <player> <TIER> <amount>
    private boolean handleDust(CommandSender sender, Player target, String[] args) {
        if (args.length < 4) {
            sender.sendMessage("§eUsage: /fegiveitem dust <player> <TIER> <amount>");
            return true;
        }
        EnchantTier tier;
        try {
            tier = EnchantTier.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cUnknown tier: §e" + args[2] + "§c. Valid: " + TIERS);
            return true;
        }
        int amount = parsePositiveInt(sender, args[3]);
        if (amount < 1) return true;

        // Give random dust tokens — identical to tinkerer output (right-click to reveal)
        for (int i = 0; i < amount; i++) {
            giveItem(target, plugin.getDustManager().createRandomDustToken(tier));
        }
        sender.sendMessage("§aGave §e" + amount + "x §7" + tier.getDisplayName() + " Random Dust Token(s) §ato §e" + target.getName() + "§a.");
        return true;
    }

    // /fegiveitem duststack <player> <TIER> [amount]  — gives 1 mystery fire-charge dust token per call
    private boolean handleDustStack(CommandSender sender, Player target, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§eUsage: /fegiveitem duststack <player> <TIER>");
            return true;
        }
        EnchantTier tier;
        try {
            tier = EnchantTier.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cUnknown tier: §e" + args[2] + "§c. Valid: " + TIERS);
            return true;
        }

        giveItem(target, plugin.getDustManager().createEnvoyDustToken(tier));
        sender.sendMessage("§aGave §7" + tier.getDisplayName() + " Mystery Dust Token (Fire Charge) §ato §e" + target.getName() + "§a.");
        return true;
    }

    // /fegiveitem xp <player> <amount>
    private boolean handleXp(CommandSender sender, Player target, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§eUsage: /fegiveitem xp <player> <amount>");
            return true;
        }
        int xpAmount = parsePositiveInt(sender, args[2]);
        if (xpAmount < 1) return true;

        ItemStack bottle = plugin.getDustManager().createStoredXpBottle(xpAmount);
        giveItem(target, bottle);
        sender.sendMessage("§aGave §eStored XP Bottle (§e" + xpAmount + " XP§a) to §e" + target.getName() + "§a.");
        return true;
    }

    // /fegiveitem blackscroll <player> [amount]
    private boolean handleBlackScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;

        for (int i = 0; i < amount; i++) {
            giveItem(target, createBlackScroll());
        }
        sender.sendMessage("§aGave §e" + amount + "x Black Scroll§a to §e" + target.getName() + "§a.");
        return true;
    }

    // /fegiveitem rerollscroll <player> [amount]
    private boolean handleRerollScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;

        for (int i = 0; i < amount; i++) {
            giveItem(target, createRerollScroll());
        }
        sender.sendMessage("§aGave §e" + amount + "x Reroll Scroll§a to §e" + target.getName() + "§a.");
        return true;
    }

    // /fegiveitem randombook <player> <TIER> [amount]
    private boolean handleRandomBook(CommandSender sender, Player target, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§eUsage: /fegiveitem randombook <player> <TIER> [amount]");
            return true;
        }
        EnchantTier tier;
        try {
            tier = EnchantTier.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cUnknown tier: §e" + args[2]);
            return true;
        }
        int amount = args.length >= 4 ? parsePositiveInt(sender, args[3]) : 1;
        if (amount < 1) return true;

        // Stack up to 64 per slot
        int remaining = amount;
        while (remaining > 0) {
            int stack = Math.min(remaining, 64);
            ItemStack book = EnchantBook.createRandomBook(tier);
            book.setAmount(stack);
            giveItem(target, book);
            remaining -= stack;
        }
        sender.sendMessage("§aGave §e" + amount + "x §" + tier.getColor() + tier.getDisplayName() + " §aRandom Enchant Book(s) to §e" + target.getName() + "§a.");
        return true;
    }

    // /fegiveitem nametag <player> [amount]
    private boolean handleNameTag(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, NameTagItem.create(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §6§lName Tag§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleWhiteScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, WhiteScrollItem.create(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §f§lWhite Scroll§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleHolyWhiteScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, HolyWhiteScrollItem.create(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §d§lHoly White Scroll§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleHeroicBlackScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, HeroicBlackScrollItem.create(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §d§lHeroic Black Scroll§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleVanillaBlackScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, VanillaBlackScrollItem.create(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §7§lVanilla Black Scroll§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleTransmogScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, TransmogScrollItem.create(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §e§lTransmog Scroll§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleGodlyTransmogScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, GodlyTransmogScrollItem.create(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §6§lGodly Transmog Scroll§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleUltimateRandomizationScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, RandomizationScrollItem.createUltimate(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §e§lUltimate Randomization Scroll§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleLegendaryRandomizationScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, RandomizationScrollItem.createLegendary(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §6§lLegendary Randomization Scroll§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleGodlyRandomizationScroll(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, RandomizationScrollItem.createGodly(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §c§lGodly Randomization Scroll§r §ato §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleSoulGem(CommandSender sender, Player target, String[] args) {
        int charges = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1000;
        if (charges < 1) return true;
        giveItem(target, SoulGemItem.create(plugin, charges));
        sender.sendMessage("§aGave §c§l✦ Soul Gem §a(§c" + charges + " §acharges) to §e" + target.getName() + "§a.");
        return true;
    }

    private boolean handleSoulGemGenerator(CommandSender sender, Player target, String[] args) {
        int amount = args.length >= 3 ? parsePositiveInt(sender, args[2]) : 1;
        if (amount < 1) return true;
        for (int i = 0; i < amount; i++) {
            giveItem(target, SoulGemItem.createGenerator(plugin));
        }
        sender.sendMessage("§aGave §e" + amount + "x §d§l✦ Random Soul Gem Generator §ato §e" + target.getName() + "§a.");
        return true;
    }

    // ─── Item creators ─────────────────────────────────────────────────────────

    private ItemStack createMysteryDust(EnchantTier tier, int amount) {
        String color = "§" + tier.getColor();
        ItemStack dust = new ItemStack(Material.SUGAR, Math.min(amount, 64));
        ItemMeta meta = dust.getItemMeta();
        meta.setDisplayName(color + "§l✦ " + tier.getDisplayName() + " Mystery Dust ✦");

        int basePercent = plugin.getConfig().getInt("random-dust.base-percent." + tier.name(), 1);
        int cap        = plugin.getConfig().getInt("random-dust.percent-cap", 15);
        int minPct = Math.max(1, basePercent);
        int maxPct = Math.min(cap, basePercent + 4);
        int dustPercent = minPct + random.nextInt(Math.max(1, maxPct - minPct + 1));

        meta.setLore(Arrays.asList(
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Apply to an enchant book to",
                "§7boost its success rate.",
                "§7",
                color + "Tier: §f" + tier.getDisplayName(),
                "§eSuccess Boost: §f+" + dustPercent + "%",
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Right-click an enchant book to apply."
        ));

        // Store PDC so BookListener can detect this as mystery dust
        NamespacedKey dustTierKey   = new NamespacedKey(plugin, "dust_tier");
        NamespacedKey dustPctKey    = new NamespacedKey(plugin, "dust_percentage");
        meta.getPersistentDataContainer().set(dustTierKey, PersistentDataType.STRING, tier.name());
        meta.getPersistentDataContainer().set(dustPctKey,  PersistentDataType.INTEGER, dustPercent);
        dust.setItemMeta(meta);
        return dust;
    }

    private ItemStack createBlackScroll() {
        ItemStack scroll = new ItemStack(Material.INK_SAC);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName("§8§l⚫ Black Scroll");
        meta.setLore(Arrays.asList(
                "§7Removes a random enchantment",
                "§7from an item and converts",
                "§7it into a §f95% §7success book.",
                "§7Place scroll on item to extract.",
                "",
                "§6§lObtainable From:",
                "§8✦ §7/bah",
                "§8✦ §7/citadel",
                "§8✦ §7/conquests",
                "§8✦ §7/dungeons: §fAbandoned Spaceship, Destroyed Outpost, Planet Nul, Time Warp",
                "§8✦ §7/end: §fElite Ender Monsters, Timeless Dragon",
                "§8✦ §7/envoys: §fLegendary",
                "§8✦ §7/kits: §fSupernova, Hyperdrive, Lucky /vkit",
                "§8✦ §7/koth",
                "§8✦ §7/minigames",
                "§8✦ §7/pquests: §fQuest Shop",
                "§8✦ §7/ruins: §fGuardians, Elite Guardians, Elder Elite Guardians, Leviathan",
                "§8✦ §7/slot: §fMeta Spin",
                "§8✦ §7/spacechests: §fSimple",
                "§8✦ §7/strongholds: §fInfernal, Frozen, Abyssal",
                "§8✦ §7/trials: §fApprentice, Hardcore, Demonic",
                "§8✦ §7/xpshop"
        ));
        NamespacedKey blackScrollKey = new NamespacedKey(plugin, "black_scroll");
        meta.getPersistentDataContainer().set(blackScrollKey, PersistentDataType.BYTE, (byte) 1);
        scroll.setItemMeta(meta);
        return scroll;
    }

    private ItemStack createRerollScroll() {
        ItemStack scroll = new ItemStack(Material.PAPER);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName("§b§l✦ Reroll Scroll §l✦");
        meta.setLore(Arrays.asList(
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "§7Reroll success/destroy rates",
                "§7on enchanted books.",
                "§7",
                "§eRight-click on a book to apply",
                "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"
        ));
        NamespacedKey rerollKey  = new NamespacedKey(plugin, "reroll_scroll");
        NamespacedKey rerollTier = new NamespacedKey(plugin, "reroll_tier");
        meta.getPersistentDataContainer().set(rerollKey,  PersistentDataType.BYTE,   (byte) 1);
        meta.getPersistentDataContainer().set(rerollTier, PersistentDataType.STRING, "ELITE");
        scroll.setItemMeta(meta);
        return scroll;
    }

    // /fegiveitem weaponorb <player> <maxSlots 10-15> [amount]
    private boolean handleWeaponOrb(CommandSender sender, Player target, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§eUsage: /fegiveitem weaponorb <player> <maxSlots 10-15> [amount]");
            return true;
        }
        int maxSlots = parsePositiveInt(sender, args[2]);
        if (maxSlots < 1) return true;
        int amount = args.length >= 4 ? parsePositiveInt(sender, args[3]) : 1;
        if (amount < 1) return true;

        for (int i = 0; i < amount; i++) {
            giveItem(target, EnchantmentOrbItem.createWeaponOrb(plugin, maxSlots));
        }
        sender.sendMessage("§aGave §e" + amount + "x Weapon Enchantment Orb [" + maxSlots + "] §ato §e" + target.getName() + "§a.");
        return true;
    }

    // /fegiveitem armororb <player> <maxSlots 10-15> [amount]
    private boolean handleArmorOrb(CommandSender sender, Player target, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§eUsage: /fegiveitem armororb <player> <maxSlots 10-15> [amount]");
            return true;
        }
        int maxSlots = parsePositiveInt(sender, args[2]);
        if (maxSlots < 1) return true;
        int amount = args.length >= 4 ? parsePositiveInt(sender, args[3]) : 1;
        if (amount < 1) return true;

        for (int i = 0; i < amount; i++) {
            giveItem(target, EnchantmentOrbItem.createArmorOrb(plugin, maxSlots));
        }
        sender.sendMessage("§aGave §e" + amount + "x Armor Enchantment Orb [" + maxSlots + "] §ato §e" + target.getName() + "§a.");
        return true;
    }

    // ─── Utility ────────────────────────────────────────────────────────────────

    private void giveItem(Player target, ItemStack item) {
        if (target.getInventory().firstEmpty() == -1) {
            target.getWorld().dropItem(target.getLocation(), item);
        } else {
            target.getInventory().addItem(item);
        }
    }

    private int parsePositiveInt(CommandSender sender, String raw) {
        try {
            int v = Integer.parseInt(raw);
            if (v < 1) throw new NumberFormatException("non-positive");
            return v;
        } catch (NumberFormatException e) {
            sender.sendMessage("§cAmount must be a positive number: §e" + raw);
            return -1;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6/fegiveitem §7sub-commands:");
        sender.sendMessage("§e  dust <player> <TIER> <amount>");
        sender.sendMessage("§e  xp <player> <amount>");
        sender.sendMessage("§e  blackscroll <player> [amount]");
        sender.sendMessage("§e  rerollscroll <player> [amount]");
        sender.sendMessage("§e  randombook <player> <TIER> [amount]");
        sender.sendMessage("§e  nametag <player> [amount]");
        sender.sendMessage("§7Tiers: " + TIERS);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) return filter(SUBS, args[0]);
        if (args.length == 2) {
            Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
            return filter(completions, args[1]);
        }
        String sub = args[0].toLowerCase();
        if (args.length == 3 && (sub.equals("dust") || sub.equals("randombook"))) {
            return filter(TIERS, args[2]);
        }
        return completions;
    }

    private List<String> filter(List<String> list, String prefix) {
        List<String> out = new ArrayList<>();
        for (String s : list) {
            if (s.toLowerCase().startsWith(prefix.toLowerCase())) out.add(s);
        }
        return out;
    }
}
