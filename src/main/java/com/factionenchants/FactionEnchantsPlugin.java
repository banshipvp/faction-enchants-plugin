package com.factionenchants;

import com.factionenchants.books.BookManager;
import com.factionenchants.books.DustManager;
import com.factionenchants.commands.AlchemistCommand;
import com.factionenchants.commands.EnchanterCommand;
import com.factionenchants.commands.EnchantsCommand;
import com.factionenchants.commands.GiveEnchantCommand;
import com.factionenchants.commands.BlessCommand;
import com.factionenchants.commands.ClearHealthCommand;
import com.factionenchants.commands.GiveItemsCommand;
import com.factionenchants.commands.GiveRandomGearCommand;
import com.factionenchants.commands.SplitSoulsCommand;
import com.factionenchants.commands.TinkererCommand;
import com.factionenchants.commands.XpShopCommand;
import com.factionenchants.enchantments.EnchantmentManager;
import com.factionenchants.gear.RandomGearManager;
import com.factionenchants.listeners.ArrowBreakListener;
import com.factionenchants.listeners.AvengingAngelListener;
import com.factionenchants.listeners.BookListener;
import com.factionenchants.listeners.FatBucketListener;
import com.factionenchants.listeners.FallenHeroListener;
import com.factionenchants.listeners.SwordBlockListener;
import com.factionenchants.listeners.FishingListener;
import com.factionenchants.listeners.CombatListener;
import com.factionenchants.listeners.EnchantListener;
import com.factionenchants.listeners.NameTagListener;
import com.factionenchants.listeners.TinkererListener;
import com.factionenchants.listeners.SoulGemListener;
import com.factionenchants.listeners.EnchantmentOrbListener;
import com.factionenchants.listeners.ExtendedLootingListener;
import com.factionenchants.listeners.OverloadCleanupListener;
import com.factionenchants.listeners.WhiteScrollListener;
import com.factionenchants.listeners.HolyWhiteScrollListener;
import com.factionenchants.listeners.BlessedEffectBlocker;
import com.factionenchants.listeners.XpShopListener;
import com.factionenchants.listeners.DeathListener;
import com.factionenchants.listeners.McMMOListener;
import com.factionenchants.listeners.TeleblockListener;
import com.factionenchants.listeners.ToolListener;
import com.factionenchants.listeners.TransmogScrollListener;
import com.factionenchants.listeners.VirusEffectListener;
import com.factionenchants.listeners.SpawnerMobTracker;
import com.factionenchants.utils.ConfigUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class FactionEnchantsPlugin extends JavaPlugin {

    private static FactionEnchantsPlugin instance;
    private EnchantmentManager enchantmentManager;
    private RandomGearManager randomGearManager;
    private BookManager bookManager;
    private DustManager dustManager;
    private ConfigUtil configUtil;
    private SoulManager soulManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("messages.yml", false);
        configUtil = new ConfigUtil(this);
        enchantmentManager = new EnchantmentManager(this);
        randomGearManager = new RandomGearManager(this);
        bookManager = new BookManager(this);
        dustManager = new DustManager(this);
        DustManager.init(this);
        soulManager = new SoulManager(this);
        enchantmentManager.registerEnchantments();
        getCommand("enchanter").setExecutor(new EnchanterCommand(this));
        getCommand("enchants").setExecutor(new EnchantsCommand(this));
        getCommand("alchemist").setExecutor(new AlchemistCommand(this));
        getCommand("tinkerer").setExecutor(new TinkererCommand(this));
        XpShopCommand xpShopCommand = new XpShopCommand(this);
        getCommand("xpshop").setExecutor(xpShopCommand);
        GiveEnchantCommand giveCmd = new GiveEnchantCommand(this);
        getCommand("fegive").setExecutor(giveCmd);
        getCommand("fegive").setTabCompleter(giveCmd);
        getCommand("fegiverandomgear").setExecutor(new GiveRandomGearCommand(this));
        GiveItemsCommand giveItemsCmd = new GiveItemsCommand(this);
        getCommand("fegiveitem").setExecutor(giveItemsCmd);
        getCommand("fegiveitem").setTabCompleter(giveItemsCmd);
        getCommand("bless").setExecutor(new BlessCommand(this));
        getCommand("splitsouls").setExecutor(new SplitSoulsCommand(this));
        getCommand("clearhealth").setExecutor(new ClearHealthCommand(this));
        getServer().getPluginManager().registerEvents(new EnchantListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new OverloadCleanupListener(this), this);
        getServer().getPluginManager().registerEvents(new BookListener(this), this);
        getServer().getPluginManager().registerEvents(new TinkererListener(this), this);
        getServer().getPluginManager().registerEvents(new XpShopListener(this, xpShopCommand), this);
        getServer().getPluginManager().registerEvents(new NameTagListener(this), this);
        getServer().getPluginManager().registerEvents(new WhiteScrollListener(this), this);
        getServer().getPluginManager().registerEvents(new HolyWhiteScrollListener(this), this);
        getServer().getPluginManager().registerEvents(new BlessedEffectBlocker(this), this);
        getServer().getPluginManager().registerEvents(new SoulGemListener(this), this);
        getServer().getPluginManager().registerEvents(new ExtendedLootingListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentOrbListener(this), this);
        getServer().getPluginManager().registerEvents(new ToolListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new VirusEffectListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnerMobTracker(), this);
        getServer().getPluginManager().registerEvents(new ArrowBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new AvengingAngelListener(this), this);
        getServer().getPluginManager().registerEvents(new FishingListener(this), this);
        getServer().getPluginManager().registerEvents(new TeleblockListener(), this);
        getServer().getPluginManager().registerEvents(new FatBucketListener(this), this);
        getServer().getPluginManager().registerEvents(new TransmogScrollListener(this), this);
        getServer().getPluginManager().registerEvents(new FallenHeroListener(this), this);
        getServer().getPluginManager().registerEvents(new SwordBlockListener(), this);
        getServer().getPluginManager().registerEvents(new SwordBlockListener(), this);
        if (getServer().getPluginManager().isPluginEnabled("mcMMO")) {
            getServer().getPluginManager().registerEvents(new McMMOListener(this), this);
            getLogger().info("mcMMO detected — Skilling and Nimble enchants activated.");
        }
        getLogger().info("FactionEnchants has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("FactionEnchants has been disabled!");
    }

    public static FactionEnchantsPlugin getInstance() { return instance; }
    public EnchantmentManager getEnchantmentManager() { return enchantmentManager; }
    public RandomGearManager getRandomGearManager() { return randomGearManager; }
    public BookManager getBookManager() { return bookManager; }
    public DustManager getDustManager() { return dustManager; }
    public ConfigUtil getConfigUtil() { return configUtil; }
    public SoulManager getSoulManager() { return soulManager; }
}
