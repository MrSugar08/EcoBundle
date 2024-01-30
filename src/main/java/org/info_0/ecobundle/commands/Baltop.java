package org.info_0.ecobundle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.info_0.ecobundle.Main;
import org.info_0.ecobundle.Util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class Baltop implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int maxPlayersToShow = Main.getInstance().getConfig().getInt("TopList.MaxPlayersToShow", 5);

        if (args.length > 0) {
            try {
                maxPlayersToShow = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Util.getMessage("Baltop-Usage"));
                return true;
            }
        }

        Map<String, Double> playerBalances = new HashMap<>();
        try {
            Connection connection = Main.getDatabase().getConnection();
            Statement statement = connection.createStatement();
            
            ResultSet resultSet = statement.executeQuery("SELECT * FROM economy ORDER BY balance DESC");

            while (resultSet.next()) {
                String playerName = resultSet.getString("player_name");
                double balance = resultSet.getDouble("balance");
                playerBalances.put(playerName, balance);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException exception) {
            Main.getDatabase().report(exception);
            return true;
        }

        List<Map.Entry<String, Double>> sortedBalances = playerBalances.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        int totalPlayers = sortedBalances.size();
        int displayCount = Math.min(maxPlayersToShow, totalPlayers);

        if (displayCount == 0) {
            sender.sendMessage("not have player");
            return true;
        }

        List<String> topList = new ArrayList<>();
        for (int i = 0; i < displayCount; i++) {
            Map.Entry<String, Double> entry = sortedBalances.get(i);
            String playerName = entry.getKey();
            double balance = entry.getValue();
            String message = Util.getMessage("Entry").replace("%p", playerName).replace("%b", String.valueOf(balance));
            topList.add(message);
        }

        sender.sendMessage(Util.getMessage("Header").replace("%t", String.valueOf(totalPlayers)));
        sender.sendMessage(topList.toArray(new String[0]));
        return true;
    }
}