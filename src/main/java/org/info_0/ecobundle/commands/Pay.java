package org.info_0.ecobundle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.info_0.ecobundle.Main;
import org.info_0.ecobundle.util.Util;

import net.milkbowl.vault.economy.Economy;

public class Pay implements CommandExecutor{

    private final Economy economy = Main.getEconomy();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(Util.getMessage("Not-Player"));
            return true;
        }
        
        if (args.length != 2) {
            sender.sendMessage(Util.getMessage("Pay-Usage"));
            return true;
        }

        String target = args[0];
        Player player = Main.getInstance().getServer().getPlayer(target);
        if (target == null) {
            sender.sendMessage(Util.getMessage("Player-Not-Online"));
            return true;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Util.getMessage("Invalid-Quantity"));
            return true;
        }

        Player senderPlayer = (Player) sender;

        double balance = economy.getBalance(senderPlayer);
        if (balance < amount) {
            sender.sendMessage(Util.getMessage("Not-Enough-Balance"));
            return true;
        }

        economy.withdrawPlayer(senderPlayer, amount);
        economy.depositPlayer(player, amount);

        sender.sendMessage(Util.getMessage("Success-Sender")
        .replace("%a", String.valueOf(amount))
        .replace("%p", player.getName()));

        player.sendMessage(Util.getMessage("Success-Receiver")
        .replace("%a", String.valueOf(amount))
        .replace("%s", senderPlayer.getName()));
        return true;
    }
    
}
