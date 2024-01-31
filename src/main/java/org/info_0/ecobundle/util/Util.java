package org.info_0.ecobundle.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.info_0.ecobundle.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Util {
    private static FileConfiguration config = Main.getInstance().getConfig();
    private static Map<File, Map<String, String>> messages = new HashMap<>();

    public static String getMessage(String messName) {
        File langFile = new File(Main.getInstance().getDataFolder() + "/lang", config.getString("lang") + ".yml");
        return messages.get(langFile).get(messName);
    }

    public static void loadMessages() {
        File langFolder = new File(Main.getInstance().getDataFolder(), "lang");
        if (!langFolder.exists()) langFolder.mkdir();
        File langFile = new File(langFolder, config.getString("lang") + ".yml");
        try {
            if (!langFile.exists()) {
                InputStream in = Main.getInstance().getResource("en-en.yml");
                Files.copy(in, langFile.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (File file : langFolder.listFiles()) {
            Map<String, String> localMessages = new HashMap<>();
            FileConfiguration lang = YamlConfiguration.loadConfiguration(file);
            for (String key : lang.getKeys(false)) {
                for (String messName : lang.getConfigurationSection(key).getKeys(false)) {
                    String message = ChatColor.translateAlternateColorCodes('&', lang.getString(key + '.' + messName));
                    localMessages.put(messName, message);
                }
            }
            messages.put(file, localMessages);
            Main.getInstance().getLogger().info(file.getName() + " loaded!");
        }
    }

    public static void reloadMessages() {
        messages.clear();
        loadMessages();
    }
}
