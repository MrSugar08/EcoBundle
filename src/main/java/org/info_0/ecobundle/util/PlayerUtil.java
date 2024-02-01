package org.info_0.ecobundle.util;

import org.bukkit.OfflinePlayer;
import org.info_0.ecobundle.Main;

public class PlayerUtil {
    public static OfflinePlayer getOfflinePlayer(String player) {
        OfflinePlayer[] players = Main.getInstance().getServer().getOfflinePlayers();

        for (OfflinePlayer op : players) {
            if (player != null && player.equals(op.getName())) {
                return op;
            }
        }

        return null;
    }

}
