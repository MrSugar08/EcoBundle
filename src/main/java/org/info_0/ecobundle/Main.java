package org.info_0.ecobundle;

import java.sql.SQLException;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.info_0.ecobundle.commands.*;
import org.info_0.ecobundle.util.Database;
import org.info_0.ecobundle.util.UpdateChecker;
import org.info_0.ecobundle.util.Util;
import org.info_0.ecobundle.vault.VaultEconomy;

import net.milkbowl.vault.economy.Economy;

public final class Main extends JavaPlugin {
	
    private static VaultEconomy economy = null;
	private static Main instance;
	private static Database db;

    @Override
    public void onEnable() {
		instance = this;
		db = new Database();
        try {
            db.connect();
            db.setup();
        } catch (SQLException exception) {
            db.report(exception);
        }
        UpdateChecker.check(this);
        UpdateChecker.sendToConsole(this);
        registerEconomy();
        getCommand("deposit").setExecutor(new Deposit());
		getCommand("withdraw").setExecutor(new Withdraw());
        getCommand("balance").setExecutor(new Balance());
	    getCommand("baltop").setExecutor(new Baltop());
        getCommand("pay").setExecutor(new Pay());
        getCommand("ecobundle").setExecutor(new EcoBundle());
		saveDefaultConfig();
		Util.loadMessages();
    }

    @Override
    public void onDisable() {
        try {
            db.getConnection().close();
        } catch (SQLException exception) {
            db.report(exception);
        }
        economy = null;
    }

    private boolean registerEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") != null) {
            economy = new VaultEconomy();
            ServicesManager sm = this.getServer().getServicesManager();
            sm.register(Economy.class, economy, this, ServicePriority.Highest);
            return true;
        } else {
            getServer().getPluginManager().disablePlugin(this);
            instance.getLogger().warning("Vault Economy not found disabling plugin!");
            return false;
        }
    }

	public static VaultEconomy getEconomy() { 
		return economy;
	}

	public static Main getInstance(){
		return instance;
	}

	public static Database getDatabase() { 
		return db;
	}
}
