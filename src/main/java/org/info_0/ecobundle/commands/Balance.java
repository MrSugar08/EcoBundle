package org.info_0.ecobundle.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.info_0.ecobundle.Main;
import org.info_0.ecobundle.util.Util;

public class Balance implements CommandExecutor {
    private final Economy economy = Main.getEconomy();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Util.getMessage("Not-Player"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            double balance = economy.getBalance(player);
            sender.sendMessage(Util.getMessage("Balance-Own-Message").replace("%s", String.valueOf(balance)));
        } else if (args.length == 1) {
            String targetPlayerName = args[0];
            Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);

            if (targetPlayer == null) {
                sender.sendMessage(Util.getMessage("Not-Player"));
                return true;
            }

            double targetBalance = economy.getBalance(targetPlayer);
            sender.sendMessage(Util.getMessage("Balance-Message")
                    .replace("%p", targetPlayer.getName())
                    .replace("%b", String.valueOf(targetBalance)));
        } else {
            sender.sendMessage(Util.getMessage("Balance-Usage"));
        }
        return true;
    }
}
