package me.herobrinedobem.heventos;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import me.herobrinedobem.heventos.databases.MySQL;
import me.herobrinedobem.heventos.databases.SQLite;
import me.herobrinedobem.heventos.utils.ConfigUtil;
import me.herobrinedobem.heventos.utils.EventoVerifyHour;
import me.herobrinedobem.heventos.utils.EventosController;
import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class HEventos extends JavaPlugin {

	private SimpleClans sc;
	private EventosController eventosController;
	private MySQL mysql;
	private SQLite sqlite;
	private Economy economy;
	private ConfigUtil configUtil;
	private final MainListeners mainListeners = new MainListeners();

	@Override
	public void onEnable() {
		try {
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fIniciando o plugin!");
			if (!new File(this.getDataFolder(), "config.yml").exists()) {
				this.saveDefaultConfig();
				Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fConfig.yml criada com sucesso!");
			} else {
				Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fConfig.yml carregada com sucesso!");
			}
			final Plugin plug = this.getServer().getPluginManager().getPlugin("SimpleClans");
			if (plug != null) {
				this.sc = ((SimpleClans) plug);
			}
			this.eventosController = new EventosController(this);
			this.eventosController.setEvento(null);
			File eventosFile;
			eventosFile = new File(this.getDataFolder() + File.separator + "Eventos");
			if (!eventosFile.exists()) {
				eventosFile.mkdirs();
				Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fPasta 'Eventos' criada com sucesso!");
			}
			if (this.getConfig().getBoolean("Ativar_Configs_Exemplos")) {
				if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "eventoexemplo.yml").exists()) {
					this.saveResource("Eventos" + File.separator + "eventoexemplo.yml", false);
				}
				if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "batataquente.yml").exists()) {
					this.saveResource("Eventos" + File.separator + "batataquente.yml", false);
				}
				if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "spleef.yml").exists()) {
					this.saveResource("Eventos" + File.separator + "spleef.yml", false);
				}
				if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "killer.yml").exists()) {
					this.saveResource("Eventos" + File.separator + "killer.yml", false);
				}
				if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "minamortal.yml").exists()) {
					this.saveResource("Eventos" + File.separator + "minamortal.yml", false);
				}
				if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "paintball.yml").exists()) {
					this.saveResource("Eventos" + File.separator + "paintball.yml", false);
				}
				Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fConfigs de exemplos criadas!");
			}
			if (this.getConfig().getBoolean("MySQL.Ativado") == false) {
				File database;
				database = new File(this.getDataFolder() + File.separator + "database.db");
				if (!database.exists()) {
					database.createNewFile();
					Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fDatabase.db criada com sucesso!");
				}
			}
			this.setupEconomy();
			this.setupSimpleClans();
			this.configUtil = new ConfigUtil();
			this.configUtil.setupConfigUtil();
			if (this.getConfig().getBoolean("MySQL.Ativado") == true) {
				this.mysql = new MySQL(this.getConfig().getString("MySQL.Usuario"), this.getConfig().getString("MySQL.Senha"), this.getConfig().getString("MySQL.Database"), this.getConfig().getString("MySQL.Host"));
				Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fMySQL Habilitado!");
			} else {
				this.sqlite = new SQLite();
				Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fSQLite Habilitado!");
			}
			this.getCommand("evento").setExecutor(new Comandos(this));
			this.getServer().getPluginManager().registerEvents(this.mainListeners, this);
			final EventoVerifyHour evh = new EventoVerifyHour();
			evh.start();
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fPlugin Habilitado - (Versao §9" + this.getDescription().getVersion() + "§f)");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		if (this.eventosController.getEvento() != null) {
			this.eventosController.getEvento().cancelEvent();
		}
		Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fPlugin Desabilitado - (Versao §9" + this.getDescription().getVersion() + "§f)");
	}

	private boolean setupEconomy() {
		final RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			this.economy = economyProvider.getProvider();
		}

		return (this.economy != null);
	}

	private void setupSimpleClans() {
		final Plugin plug = this.getServer().getPluginManager().getPlugin("SimpleClans");
		if (plug != null) {
			this.sc = ((SimpleClans) plug);
		}
	}

	public SQLite getSqlite() {
		return this.sqlite;
	}

	public ConfigUtil getConfigUtil() {
		return this.configUtil;
	}

	public Economy getEconomy() {
		return this.economy;
	}

	public static HEventos getHEventos() {
		return (HEventos) Bukkit.getServer().getPluginManager().getPlugin("HEventos");
	}

	public EventosController getEventosController() {
		return this.eventosController;
	}

	public MySQL getMysql() {
		return this.mysql;
	}

	public SimpleClans getSc() {
		return this.sc;
	}

}
