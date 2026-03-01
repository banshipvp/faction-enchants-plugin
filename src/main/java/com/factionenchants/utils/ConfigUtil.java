package com.factionenchants.utils;

import com.factionenchants.FactionEnchantsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigUtil {

    private final FactionEnchantsPlugin plugin;
    private FileConfiguration messagesConfig;

    public ConfigUtil(FactionEnchantsPlugin plugin) {
        this.plugin = plugin;
        loadMessages();
    }

    private void loadMessages() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) plugin.saveResource("messages.yml", false);
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(String key) {
        String prefix = messagesConfig.getString("prefix", "8[6FactionEnchants8] r");
        String msg = messagesConfig.getString(key, "cMessage not found: " + key);
        return colorize(prefix + msg);
    }

    public static String colorize(String text) {
        return text.replace("&", "");
    }
}
