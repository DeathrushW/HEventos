package me.herobrinedobem.heventos.eventos;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.listeners.MinaMortalListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.Cuboid;

public class MinaMortal extends EventoBase {

	private MinaMortalListener listener;
	private int tempoDeEvento, tempoDeEventoCurrent, tempoMensagens,
			tempoMensagensCurrent;

	public MinaMortal(YamlConfiguration config) {
		super(config);
		listener = new MinaMortalListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(listener, HEventos.getHEventos());
		tempoDeEvento = config.getInt("Config.Evento_Tempo_Minutos") * 60;
		tempoMensagens = config.getInt("Config.Mensagens_Tempo_Minutos") * 60;
		tempoDeEventoCurrent = tempoDeEvento;
		tempoMensagensCurrent = tempoMensagens;
		Cuboid cubo = new Cuboid(HEventosAPI.getLocation(getConfig(), "Localizacoes.Mina_1"), HEventosAPI.getLocation(getConfig(), "Localizacoes.Mina_2"));
		ArrayList<String> blocosConfig = new ArrayList<>();
		for (String s : getConfig().getString("Config.Minerios").split(";")) {
			blocosConfig.add(s);
		}
		for (Block b : cubo.getBlocks()) {
			Random r = new Random();
			if (r.nextInt(100) <= getConfig().getInt("Config.Porcentagem_De_Minerios")) {
				String bloco = blocosConfig.get(r.nextInt(blocosConfig.size()));
				b.setType(Material.getMaterial(Integer.parseInt(bloco)));
			} else {
				b.setType(Material.STONE);
			}
		}
	}
	
	@Override
	public void startEventMethod() {
		for (String s : getParticipantes()) {
			getPlayerByName(s).teleport(HEventosAPI.getLocation(getConfig(), "Localizacoes.Entrada"));
		}
	}

	@Override
	public void scheduledMethod() {
		if ((isOcorrendo() == true) && (isAberto() == false)) {
			if (getParticipantes().size() > 0) {
				if (tempoDeEventoCurrent > 0) {
					tempoDeEventoCurrent--;
					if (tempoMensagensCurrent == 0) {
						for (String s : getConfig().getStringList("Mensagens.Status")) {
							HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$tempo$", tempoDeEventoCurrent + ""));
						}
						tempoMensagensCurrent = tempoMensagens;
					} else {
						tempoMensagensCurrent--;
					}
				} else {
					stopEvent();
				}
			} else {
				stopEvent();
			}
		}
	}

	@Override
	public void stopEventMethod() {
		sendMessageList("Mensagens.Finalizado");
	}

	@Override
	public void cancelEventMethod() {
		sendMessageList("Mensagens.Cancelado");
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		tempoDeEvento = getConfig().getInt("Config.Evento_Tempo_Minutos") * 60;
		tempoMensagens = getConfig().getInt("Config.Mensagens_Tempo_Minutos") * 60;
		tempoDeEventoCurrent = tempoDeEvento;
		tempoMensagensCurrent = tempoMensagens;
		BukkitEventHelper.unregisterEvents(listener, HEventos.getHEventos());
	}

	public int getTempoDeEvento() {
		return this.tempoDeEvento;
	}

	public void setTempoDeEvento(int tempoDeEvento) {
		this.tempoDeEvento = tempoDeEvento;
	}

	public int getTempoDeEventoCurrent() {
		return this.tempoDeEventoCurrent;
	}

	public void setTempoDeEventoCurrent(int tempoDeEventoCurrent) {
		this.tempoDeEventoCurrent = tempoDeEventoCurrent;
	}

	public int getTempoMensagens() {
		return this.tempoMensagens;
	}

	public void setTempoMensagens(int tempoMensagens) {
		this.tempoMensagens = tempoMensagens;
	}

	public int getTempoMensagensCurrent() {
		return this.tempoMensagensCurrent;
	}

	public void setTempoMensagensCurrent(int tempoMensagensCurrent) {
		this.tempoMensagensCurrent = tempoMensagensCurrent;
	}

	public MinaMortalListener getListener() {
		return this.listener;
	}

}
