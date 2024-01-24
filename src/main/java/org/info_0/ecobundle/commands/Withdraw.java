package org.info_0.ecobundle.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.info_0.ecobundle.EcoBundle;
import org.info_0.ecobundle.Util.MaterialCalculator;
import org.info_0.ecobundle.Util.Util;

public class Withdraw implements CommandExecutor {
	private final Economy economy = EcoBundle.getEconomy();

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (!(commandSender instanceof Player)) {
			commandSender.sendMessage(Util.getMessage("Not-Player"));
			return false;
		}

		Player player = (Player) commandSender;

		if (!player.hasPermission("eco-bundle.withdraw")) {
			player.sendMessage(Util.getMessage("No-Permission"));
			return false;
		}

		if (strings.length != 1) {
			player.sendMessage(Util.getMessage("Withdraw-Usage"));
			return false;
		}

		int amount;
		boolean isAll;

		try {
			if (strings[0].equalsIgnoreCase("all")) {
				amount = MaterialCalculator.emptySpace(player);
				isAll = true;
			} else {
				amount = Integer.parseInt(strings[0]);
				isAll = false;
			}
		} catch (NumberFormatException e) {
			player.sendMessage(Util.getMessage("Withdraw-Usage"));
			throw new RuntimeException(e);
		}

		if (amount <= 0) {
			player.sendMessage(Util.getMessage("Not-Zero"));
			return false;
		}

		int money = amount * 5;
		if (economy.getBalance(player) < money){
			if (economy.getBalance(player) < 5) {
				player.sendMessage(Util.getMessage("No-Money"));
				return false;
			} else {
				player.sendMessage(Util.getMessage("Not-Enough-Balance"));
				money = (int) (economy.getBalance(player) - economy.getBalance(player) % 5);
				amount = money / 5;
			}
		}
		
		economy.withdrawPlayer(player, money);
		player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT,amount));
		player.sendMessage((isAll) ? Util.getMessage("Withdraw-All-Message") :
			String.format(Util.getMessage("Withdraw-Message"), amount));
		return true;
	}
}
