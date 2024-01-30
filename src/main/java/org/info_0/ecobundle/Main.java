package org.info_0.ecobundle;

import java.sql.SQLException;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.info_0.ecobundle.Util.Database;
import org.info_0.ecobundle.Util.Util;
import org.info_0.ecobundle.commands.*;
import org.info_0.ecobundle.vault.VaultEconomy;

import net.milkbowl.vault.economy.Economy;

public final class Main extends JavaPlugin {
	
    private static Economy economy = null;
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
            final ServicesManager sm = this.getServer().getServicesManager();
            sm.register(Economy.class, new VaultEconomy(), this, ServicePriority.Highest);
            instance.getLogger().info("Registered Vault interface.");
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            
            if (rsp != null) {
                economy = rsp.getProvider();
            }
            return true;
        } else {
            instance.getLogger().severe("Vault not found. Please download Vault to use iConomy " + getDescription().getVersion().toString() + ".");
            return false;
        }
    }

	public static Economy getEconomy() { 
		return economy;
	}

	public static Main getInstance(){
		return instance;
	}

	public static Database getDatabase() { 
		return db;
	}
}
