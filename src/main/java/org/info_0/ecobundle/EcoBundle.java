package org.info_0.ecobundle;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;
import org.info_0.ecobundle.Util.Database;
import org.info_0.ecobundle.Util.Util;
import org.info_0.ecobundle.commands.*;
import org.info_0.ecobundle.vault.VaultEconomy;

public final class EcoBundle extends JavaPlugin {
	
    private static VaultEconomy economy;
	private static EcoBundle instance;
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
		economy = new VaultEconomy();
		getCommand("deposit").setExecutor(new Deposit());
		getCommand("withdraw").setExecutor(new Withdraw());
        getCommand("balance").setExecutor(new Balance());
	    getCommand("baltop").setExecutor(new Baltop());
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
    }

	public static VaultEconomy getEconomy() { 
		return economy;
	}

	public static EcoBundle getInstance(){
		return instance;
	}

	public static Database getDatabase() { 
		return db;
	}
}
