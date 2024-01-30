package org.info_0.ecobundle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.info_0.ecobundle.Main;
import org.info_0.ecobundle.Util.Util;

public class EcoBundle implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender.hasPermission("eco-bundle.ecobundle"))) {
            sender.sendMessage(Util.getMessage("No-Permission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(":)");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) { 
            Util.reloadMessages();
        }

        if (args[0].equalsIgnoreCase("eco") && args[1].equalsIgnoreCase("set")) {
            String targetName = args[2];
            double amount;

            try {
                amount = Double.parseDouble(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Util.getMessage("Invalid-Amount"));
                return false;
            }

            Player target = sender.getServer().getPlayer(targetName);
            if (target != null) {
                sender.sendMessage(Util.getMessage("Eco-Set-Success").replace("%p", target.getName()).replace("%b", String.valueOf(amount)));
                Main.getEconomy().setBalance(target, amount);
                return true;
            } else {
                sender.sendMessage(Util.getMessage("Player-Not-Online"));
                return false;
            }
        }
        return true;
    }
}
