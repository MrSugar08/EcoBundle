package org.info_0.ecobundle.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class UpdateChecker {
    private static boolean upToDate = true;
    private static String version = "";

    public static void sendToConsole(Plugin plugin) {
        String currentVersion = plugin.getDescription().getVersion();

        if (upToDate) {
            plugin.getLogger().info("You are using the latest version of EcoBundle!");
        } else {
            List<String> lines = new ArrayList<>();
            lines.add("There is a new version of EcoBundle!");
            lines.add(" ");
            lines.add(String.format("Your version: %s%s", ChatColor.RED, currentVersion));
            lines.add(String.format("Latest version: %s%s", ChatColor.GREEN, version));
            lines.add(" ");
            lines.add("Please update to the latest version.");
            lines.add(" ");

            printNiceBoxToConsole(plugin.getLogger(), lines);
        }
    }

    public static void sendToPlayer(Plugin plugin, Player player) {
        String currentVersion = plugin.getDescription().getVersion();

        if (!upToDate) {
            final ComponentBuilder lore = new ComponentBuilder("Link: ").bold(true).append("https://github.com/MrSugar08/EcoBundle").bold(false);
            final TextComponent component = new TextComponent("Please click the update");
            component.setBold(true);
            component.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/MrSugar08/EcoBundle"));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, lore.create()));

            player.sendMessage(ChatColor.GOLD + "EcoBundle" + ChatColor.GRAY + " have new version!");
            player.sendMessage(ChatColor.DARK_GRAY + "Latest version: " + ChatColor.GREEN + version + ChatColor.DARK_GRAY + " | Your version: " + ChatColor.RED + currentVersion);
            player.spigot().sendMessage(component);
            player.sendMessage("");
        }
    }


    public static void check(Plugin plugin) {
        String currentVersion = plugin.getDescription().getVersion();

        try {
            URL url = new URL("https://api.github.com/repos/MrSugar08/EcoBundle/releases");
            InputStream is = url.openStream();
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            JsonArray array = new Gson().fromJson(br, JsonArray.class);
            JsonObject release = array.get(0).getAsJsonObject();

            version = release.get("tag_name").getAsString();
            upToDate = version.equals(currentVersion);
        } catch (Throwable t) {
            version = currentVersion;
        }
    }

    private static void printNiceBoxToConsole(Logger logger, List<String> lines) {
        int longestLine = 0;
        for (String line : lines) {
            longestLine = Math.max(line.length(), longestLine);
        }
        longestLine += 2;
        if (longestLine > 120) longestLine = 120;
        longestLine += 2;
        StringBuilder dash = new StringBuilder(longestLine);
        Stream.generate(() -> "*").limit(longestLine).forEach(dash::append);

        logger.log(Level.WARNING, dash.toString());
        for (String line : lines) {
            logger.log(Level.WARNING, ("*" + " ") + line);
        }
        logger.log(Level.WARNING, dash.toString());
    }


}