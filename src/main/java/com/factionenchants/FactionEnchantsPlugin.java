package com.factionenchants;

import com.factionenchants.books.BookManager;
import com.factionenchants.books.DustManager;
import com.factionenchants.commands.AlchemistCommand;
import com.factionenchants.commands.EnchanterCommand;
import com.factionenchants.commands.EnchantsCommand;
import com.factionenchants.commands.GiveEnchantCommand;
import com.factionenchants.commands.GiveRandomGearCommand;
import com.factionenchants.commands.TinkererCommand;
import com.factionenchants.commands.XpShopCommand;
import com.factionenchants.enchantments.EnchantmentManager;
import com.factionenchants.gear.RandomGearManager;
import com.factionenchants.listeners.BookListener;
import com.factionenchants.listeners.CombatListener;
import com.factionenchants.listeners.EnchantListener;
import com.factionenchants.listeners.TinkererListener;
import com.factionenchants.listeners.XpShopListener;
import com.factionenchants.utils.ConfigUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class FactionEnchantsPlugin extends JavaPlugin {

    private static FactionEnchantsPlugin instance;
    private EnchantmentManager enchantmentManager;
    private RandomGearManager randomGearManager;
    private BookManager bookManager;
    private DustManager dustManager;
    private ConfigUtil configUtil;

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
        getServer().getPluginManager().registerEvents(new EnchantListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new BookListener(this), this);
        getServer().getPluginManager().registerEvents(new TinkererListener(this), this);
        getServer().getPluginManager().registerEvents(new XpShopListener(this, xpShopCommand), this);
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
}
