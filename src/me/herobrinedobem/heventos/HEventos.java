package me.herobrinedobem.heventos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import com.p000ison.dev.simpleclans2.api.SCCore;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.listeners.EventoStopEvent;
import me.herobrinedobem.heventos.databases.Database;
import me.herobrinedobem.heventos.databases.DatabaseType;
import me.herobrinedobem.heventos.utils.ConfigUtil;
import me.herobrinedobem.heventos.utils.EventoCancellType;
import me.herobrinedobem.heventos.utils.EventoVerifyHour;
import me.herobrinedobem.heventos.utils.EventosController;
import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

/**
 * Classe principal do projeto, aqui onde o plugin sera iniciado e todas as suas funcões serão chamadas!
 * @author Herobrinedobem (Gabriel Henrique)
 * @version 1.6
 * @see EventosController
 * @see MySQL
 * @see SQLite
 * @see ConfigUtil
 * @see MainListeners
 * @see EventoStopEvent
 * @see EventoVerifyHour
 */
public class HEventos extends JavaPlugin {

	//Váriavel onde ficarao salvos os eventos externos por meio da API do projeto, para que assim possam ser chamados futuramente automaticamente
	private ArrayList<EventoBase> externalEventos = new ArrayList<>();
	//Váriavel da classe que controla os eventos
	private EventosController eventosController;
	//Váriavel da classe controla o banco de dados do plugin
	private Database databaseManager;
	//Váriaveis dos plugins que o projeto é dependente, sendo o de economia e o de clans
	private Economy economy;
	private SimpleClans sc;
	private SCCore core;
	//Váriavel da classe que controla os métodos uteis relacionados a config.yml
	private ConfigUtil configUtil;
	//Váriavel da classe que controla os principais listeners do projeto
	private MainListeners mainListeners = new MainListeners();
	//Váriavel da classe que controla o horário de inicio dos eventos automáticos
	private EventoVerifyHour evh;

	/**
	 * Método que é chamado quando o plugin iniciar, serao criadas as configs, as dependencias serao ligadas
	 * a database sera selecionada, os comandos e listeners serão criados e a instancia das classes {@link EventosController} e {@link EventoVerifyHour} serão criadas.
	 */
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fIniciando o plugin!");
		loadConfigs();
		loadDependencies();
		loadDatabase();
		loadCommands();
		loadListeners();
		loadEventos();
		Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fPlugin Habilitado - (Versao §9" + this.getDescription().getVersion() + "§f)");
	}
	
	/**
	 * Método que será chamado quando o plugin for desativado, nele iremos verificar se existe algum evento ocorrendo, se tiver iremos cancelar
	 * e mandar os jogadores para o local de saida.
	 */
	@Override
	public void onDisable() {
		//Verificamos se nao há nenhum evento ocorrendo no momento
		if (this.eventosController.getEvento() != null) {
			//Caso tenha algum evento ocorrendo criamos o listener relacionado a quando um evento é cancelado
			EventoStopEvent event = new EventoStopEvent(HEventos.getHEventos().getEventosController().getEvento(), EventoCancellType.SERVER_STOPED);
			HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
			//Cancelando o evento
			this.eventosController.getEvento().cancelEvent();
			eventosController.setEvento(null);
		}
		Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fPlugin Desabilitado - (Versao §9" + this.getDescription().getVersion() + "§f)");
	}
	
	/**
	 * Método que irá carregar as configs do plugin.
	 */
	private void loadConfigs(){
		//Verificamos se o arquivo config.yml existe, caso não exista o criamos
		if (!new File(this.getDataFolder(), "config.yml").exists()) {
			this.saveDefaultConfig();
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fConfig.yml criada com sucesso!");
		} else {
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fConfig.yml carregada com sucesso!");
		}
		
		//Verificamos se pasta Eventos existe, caso não exista a criamos
		File eventosFile = new File(this.getDataFolder() + File.separator + "Eventos");
		if (!eventosFile.exists()) {
			eventosFile.mkdirs();
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fPasta 'Eventos' criada com sucesso!");
		}
		
		//Verificamos na config se está habilitado para criar os arquivos de exemplos de eventos, caso esteja copiamos os exemplos para a pasta Eventos
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
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "semaforo.yml").exists()) {
				this.saveResource("Eventos" + File.separator + "semaforo.yml", false);
			}
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fConfigs de exemplos criadas!");
		}
		
		//Instanciamos a classe ConfigUtils
		this.configUtil = new ConfigUtil();
	}
	
	/**
	 * Método que irá carregar as dependencias do plugin.
	 */
	private void loadDependencies(){
		//Carregando a dependencia do SimpleClans
		if(!hookSimpleClans()){
			setupSimpleClans();
		}
		//Carregando a dependencia do Vault
		setupEconomy();
	}
	
	/**
	 * Método que irá carregar o banco de dados do plugin.
	 */
	private void loadDatabase(){
		if (this.getConfig().getBoolean("MySQL.Ativado") == true) {
			databaseManager = new Database(DatabaseType.MYSQL, this.getConfig().getString("MySQL.Usuario"), this.getConfig().getString("MySQL.Senha"), this.getConfig().getString("MySQL.Database"), this.getConfig().getString("MySQL.Host"));
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fMySQL Habilitado!");
		} else {
			File databaseFile;
			databaseFile = new File(this.getDataFolder() + File.separator + "database.db");
			if (!databaseFile.exists()) {
				try {
					databaseFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fDatabase.db criada com sucesso!");
			}
			databaseManager = new Database(DatabaseType.SQLITE);
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fSQLite Habilitado!");
		}
	}
	
	/**
	 * Método que irá carregar os comandos do plugin.
	 */
	private void loadCommands(){
		this.getCommand("evento").setExecutor(new Comandos(this));
	}
	
	/**
	 * Método que irá carregar os listeners do plugin.
	 */
	private void loadListeners(){
		this.getServer().getPluginManager().registerEvents(this.mainListeners, this);
	}
	
	/**
	 * Método que irá criar a instancia das classes {@link EventosController} e {@link EventoVerifyHour}.
	 */
	private void loadEventos(){
		this.eventosController = new EventosController(this);
		this.eventosController.setEvento(null);
		evh = new EventoVerifyHour();
		evh.start();
	}

	private boolean setupEconomy() {
		final RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fVault encontrado com sucesso!");
			this.economy = economyProvider.getProvider();
		}

		return (this.economy != null);
	}

	private void setupSimpleClans() {
		final Plugin plug = this.getServer().getPluginManager().getPlugin("SimpleClans");
		if (plug != null) {
			Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fSimpleClans 1 encontrado com sucesso!");
			this.sc = ((SimpleClans) plug);
		}
	}
	
	private boolean hookSimpleClans(){
        try {
            for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
                if (plugin instanceof SCCore) {
                	Bukkit.getConsoleSender().sendMessage("§9[HEventos] §fSimpleClans 2 encontrado com sucesso!");
                    this.core = (SCCore) plugin;
                    return true;
                }
            }
        } catch (NoClassDefFoundError e) {
            return false;
        }
        return false;
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

	public SimpleClans getSc() {
		return this.sc;
	}
	
	public Database getDatabaseManager() {
		return databaseManager;
	}
	
	public ArrayList<EventoBase> getExternalEventos() {
		return externalEventos;
	}

	public SCCore getCore() {
		return core;
	}
	
}
