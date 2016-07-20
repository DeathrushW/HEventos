package me.herobrinedobem.heventos.eventos;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;
import me.herobrinedobem.heventos.listeners.KillerListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class Killer extends EventoBase {

	private KillerListener listener;
	private boolean msgTempo = false;
	private int tempoMensagens, tempoMensagensCurrent, tempoPegarItens,
			tempoPegarItensCurrent;

	public Killer(YamlConfiguration config) {
		super(config);
		listener = new KillerListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(listener, HEventos.getHEventos());
		tempoPegarItens = config.getInt("Config.Tempo_Pegar_Itens");
		tempoPegarItensCurrent = config.getInt("Config.Tempo_Pegar_Itens");
		tempoMensagens = config.getInt("Config.Mensagens_Tempo_Minutos") * 60;
		tempoMensagensCurrent = tempoMensagens;
	}

	@Override
	public void startEventMethod() {
		getEntrada().getWorld().setTime(17000);
		for (String s : getParticipantes()) {
			getPlayerByName(s).teleport(HEventosAPI.getLocation(getConfig(), "Localizacoes.Entrada"));
		}
	}

	@Override
	public void scheduledMethod() {
		if ((isOcorrendo() == true) && (isAberto() == false)) {
			if (getParticipantes().size() > 1) {
				if (tempoMensagensCurrent == 0) {
					for (String s : getConfig().getStringList("Mensagens.Status")) {
						HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$jogadores$", getParticipantes().size() + ""));
					}
					tempoMensagensCurrent = tempoMensagens;
				} else {
					tempoMensagensCurrent--;
				}
			} else {
				if (getParticipantes().size() == 1) {
					if (tempoPegarItensCurrent == 0) {
						Player player = null;
						for(String s : getParticipantes()){
							player = getPlayerByName(s);
						}
						EventoPlayerWinEvent event = new EventoPlayerWinEvent(player, this, 0);
						HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
						stopEvent();
					} else {
						if (msgTempo == false) {
							for (String s : getParticipantes()) {
								getPlayerByName(s).sendMessage(getConfig().getString("Mensagens.Tempo_Pegar_Itens").replace("&", "§"));
							}
							msgTempo = true;
						}
						tempoPegarItensCurrent--;
					}
				} else {
					sendMessageList("Mensagens.Sem_Vencedor");
					stopEvent();
				}
			}
		}
	}

	@Override
	public void cancelEventMethod() {
		sendMessageList("Mensagens.Cancelado");
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		tempoPegarItens = getConfig().getInt("Config.Tempo_Pegar_Itens");
		tempoPegarItensCurrent = getConfig().getInt("Config.Tempo_Pegar_Itens");
		tempoMensagens = getConfig().getInt("Config.Mensagens_Tempo_Minutos") * 60;
		tempoMensagensCurrent = tempoMensagens;
		BukkitEventHelper.unregisterEvents(listener, HEventos.getHEventos());
	}

	public boolean isMsgTempo() {
		return this.msgTempo;
	}

	public void setMsgTempo(boolean msgTempo) {
		this.msgTempo = msgTempo;
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

	public int getTempoPegarItens() {
		return this.tempoPegarItens;
	}

	public void setTempoPegarItens(int tempoPegarItens) {
		this.tempoPegarItens = tempoPegarItens;
	}

	public int getTempoPegarItensCurrent() {
		return this.tempoPegarItensCurrent;
	}

	public void setTempoPegarItensCurrent(int tempoPegarItensCurrent) {
		this.tempoPegarItensCurrent = tempoPegarItensCurrent;
	}

	public KillerListener getListener() {
		return this.listener;
	}

}
