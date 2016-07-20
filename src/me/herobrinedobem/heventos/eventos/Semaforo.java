package me.herobrinedobem.heventos.eventos;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;
import me.herobrinedobem.heventos.listeners.SemaforoListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class Semaforo extends EventoBase {

	private SemaforoListener listener;
	private int tempoTroca;
	private int tempoTrocaCurrent;
	private boolean podeAndar;

	public Semaforo(YamlConfiguration config) {
		super(config);
		listener = new SemaforoListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(listener, HEventos.getHEventos());
		tempoTroca = config.getInt("Config.Tempo_Troca");
		tempoTrocaCurrent = config.getInt("Config.Tempo_Troca");
		podeAndar = true;
	}

	@Override
	public void startEventMethod() {
		for (String s : getParticipantes()) {
			for (String msg : getConfig().getStringList("Mensagens.Verde")) {
				getPlayerByName(s).sendMessage(msg.replace("&", "§"));
			}
			getPlayerByName(s).teleport(HEventosAPI.getLocation(getConfig(), "Localizacoes.Entrada"));
		}
	}

	@Override
	public void scheduledMethod() {
		if ((isOcorrendo() == true) && (isAberto() == false)) {
			if (tempoTrocaCurrent == 0) {
				podeAndar = true;
				for (String s : getParticipantes()) {
					for (String msg : getConfig().getStringList("Mensagens.Verde")) {
						getPlayerByName(s).sendMessage(msg.replace("&", "§"));
					}
				}
				tempoTrocaCurrent = tempoTroca;
			} else if (tempoTrocaCurrent == (tempoTroca / 2)) {
				for (String s : getParticipantes()) {
					for (String msg : getConfig().getStringList("Mensagens.Amarelo")) {
						getPlayerByName(s).sendMessage(msg.replace("&", "§"));
					}
				}
				tempoTrocaCurrent--;
			} else if (tempoTrocaCurrent == 5) {
				for (String s : getParticipantes()) {
					for (String msg : getConfig().getStringList("Mensagens.Vermelho")) {
						getPlayerByName(s).sendMessage(msg.replace("&", "§"));
					}
				}
				podeAndar = false;
				tempoTrocaCurrent--;
			} else {
				tempoTrocaCurrent--;
			}
			
			if (getParticipantes().size() <= 0) {
				sendMessageList("Mensagens.Sem_Vencedor");
				stopEvent();
			}else if(getParticipantes().size() == 1){
				Player player = null;
				for(String s : getParticipantes()){
					player = getPlayerByName(s);
				}
				EventoPlayerWinEvent event = new EventoPlayerWinEvent(player, this, 0);
				HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
				stopEvent();
			}
		}
	}

	@Override
	public void cancelEventMethod() {
		this.sendMessageList("Mensagens.Cancelado");
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		tempoTroca = this.getConfig().getInt("Config.Tempo_Troca");
		tempoTrocaCurrent = this.getConfig().getInt("Config.Tempo_Troca");
		podeAndar = true;
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	public int getTempoTrocaCurrent() {
		return this.tempoTrocaCurrent;
	}

	public void setTempoTrocaCurrent(int tempoTrocaCurrent) {
		this.tempoTrocaCurrent = tempoTrocaCurrent;
	}

	public boolean isPodeAndar() {
		return this.podeAndar;
	}

	public void setPodeAndar(boolean podeAndar) {
		this.podeAndar = podeAndar;
	}

	public int getTempoTroca() {
		return this.tempoTroca;
	}

}
