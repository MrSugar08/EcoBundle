package org.info_0.ecobundle.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.info_0.ecobundle.EcoBundle;
import org.info_0.ecobundle.Util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class Baltop implements CommandExecutor {

    private final Economy economy = EcoBundle.getEconomy();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int maxPlayersToShow = EcoBundle.getInstance().getConfig().getInt("TopList.MaxPlayersToShow", 5);

        Map<String, Double> playerBalances = new HashMap<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            double balance = economy.getBalance(onlinePlayer);
            playerBalances.put(onlinePlayer.getName(), balance);
        }

        List<Map.Entry<String, Double>> sortedBalances = playerBalances.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());


        List<String> topList = new ArrayList<>();
        for (int i = 0; i < Math.min(maxPlayersToShow, sortedBalances.size()); i++) {
            Map.Entry<String, Double> entry = sortedBalances.get(i);
            String playerName = entry.getKey();
            double balance = entry.getValue();
            String message = Util.getMessage("Entry").replace("%p", playerName).replace("%b", String.valueOf(balance));
            topList.add(message);
        }

        sender.sendMessage(Util.getMessage("Header"));
        sender.sendMessage(topList.toArray(new String[0]));
        return true;
    }
}
