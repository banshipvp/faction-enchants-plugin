package com.factionenchants.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void send(Player player, String message) {
        player.sendMessage(colorize(message));
    }

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes(chr38, text);
    }

    private static final char chr38 = 38;

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(colorize(title), colorize(subtitle), fadeIn, stay, fadeOut);
    }
}
