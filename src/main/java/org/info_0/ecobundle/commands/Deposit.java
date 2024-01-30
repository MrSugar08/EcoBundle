package org.info_0.ecobundle.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.info_0.ecobundle.Main;
import org.info_0.ecobundle.Util.MaterialCalculator;
import org.info_0.ecobundle.Util.Util;

public class Deposit implements CommandExecutor {
	private final Economy economy = Main.getEconomy();
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (!(commandSender instanceof Player)) {
			commandSender.sendMessage(Util.getMessage("Not-Player"));
			return false;
		}

		Player player = (Player) commandSender;

		if (!player.hasPermission("eco-bundle.deposit")) {
			player.sendMessage(Util.getMessage("No-Permission"));
			return false;
		}

		if (strings.length != 1) {
			player.sendMessage(Util.getMessage("Deposit-Usage"));
			return false;
		}

		int amount;
		boolean isAll;

		try {
			if (strings[0].equalsIgnoreCase("all")) {
				amount = MaterialCalculator.goldAmount(player);
				isAll = true;
			} else {
				amount = Integer.parseInt(strings[0]);
				isAll = false;
			}
		} catch (NumberFormatException e) {
			player.sendMessage(Util.getMessage("Deposit-Usage"));
			throw new RuntimeException(e);
		}

		if (amount <= 0) {
			player.sendMessage(Util.getMessage("Not-Zero"));
			return false;
		}

		int playerGoldAmount = MaterialCalculator.goldAmount(player);

		if (playerGoldAmount == 0) {
			player.sendMessage(Util.getMessage("No-Gold"));
			return false;
		}

		if (amount > playerGoldAmount) {
			player.sendMessage(Util.getMessage("Not-Enough-Gold"));
			amount = playerGoldAmount;
			isAll = true;
		}

		int money = amount * Main.getInstance().getConfig().getInt("gold_amount");
		economy.depositPlayer(player, money);
		player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT,amount));
		player.sendMessage((isAll) ? Util.getMessage("Deposit-All-Message") :
			String.format(Util.getMessage("Deposit-Message"), amount));
		
		return true;
	}
}
