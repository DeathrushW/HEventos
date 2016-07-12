package me.herobrinedobem.examples.spleef;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import me.herobrinedobem.heventos.api.HEventosAPI;

public class Spleef extends JavaPlugin{

	@Override
	public void onEnable() {
		if(!new File(getDataFolder(), "config.yml").exists()){
			saveDefaultConfig();
		}
		HEventosAPI.getExternalEventos().add(new SpleefBehaviour((YamlConfiguration) getConfig()));
		getServer().getPluginManager().registerEvents(new SpleefListeners(), this);
		System.out.println("Plugin Habilitado");
	}

	@Override
	public void onDisable() {
		System.out.println("Plugin Desabilitado");
	}
	
}
