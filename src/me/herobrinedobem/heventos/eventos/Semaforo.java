package me.herobrinedobem.heventos.eventos;

import org.bukkit.configuration.file.YamlConfiguration;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.listeners.SemaforoListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.EventoBase;

public class Semaforo extends EventoBase {

	private final SemaforoListener listener;
	private int tempoTroca;
	private int tempoTrocaCurrent;
	private boolean podeAndar;

	public Semaforo(final YamlConfiguration config) {
		super(config);
		this.listener = new SemaforoListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(this.listener, HEventos.getHEventos());
		this.tempoTroca = config.getInt("Config.Tempo_Troca");
		this.tempoTrocaCurrent = config.getInt("Config.Tempo_Troca");
		this.podeAndar = true;
	}

	@Override
	public void startEventMethod() {
		for (final String s : this.getParticipantes()) {
			for (final String msg : this.getConfig().getStringList("Mensagens.Verde")) {
				this.getPlayerByName(s).sendMessage(msg.replace("&", "§"));
			}
		}
	}

	@Override
	public void scheduledMethod() {
		if ((this.isOcorrendo() == true) && (this.isAberto() == false)) {
			if (this.tempoTrocaCurrent <= 0) {
				if (this.podeAndar) {
					this.podeAndar = false;
					this.tempoTrocaCurrent = this.tempoTroca;
				} else {
					this.podeAndar = true;
					for (final String s : this.getParticipantes()) {
						for (final String msg : this.getConfig().getStringList("Mensagens.Verde")) {
							this.getPlayerByName(s).sendMessage(msg.replace("&", "§"));
						}
					}
					this.tempoTrocaCurrent = this.tempoTroca;
				}
			} else if (this.tempoTrocaCurrent == (this.tempoTroca / 2)) {
				if (this.podeAndar) {
					for (final String s : this.getParticipantes()) {
						for (final String msg : this.getConfig().getStringList("Mensagens.Amarelo")) {
							this.getPlayerByName(s).sendMessage(msg.replace("&", "§"));
						}
					}
				}
				this.tempoTrocaCurrent--;
			} else if (this.tempoTrocaCurrent == 2) {
				if (this.podeAndar) {
					for (final String s : this.getParticipantes()) {
						for (final String msg : this.getConfig().getStringList("Mensagens.Vermelho")) {
							this.getPlayerByName(s).sendMessage(msg.replace("&", "§"));
						}
					}
				}
				this.tempoTrocaCurrent--;
			} else {
				this.tempoTrocaCurrent--;
			}
			if (this.getParticipantes().size() <= 0) {
				this.sendMessageList("Mensagens.Sem_Vencedor");
				this.stopEvent();
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
		this.tempoTroca = this.getConfig().getInt("Config.Tempo_Troca");
		this.tempoTrocaCurrent = this.getConfig().getInt("Config.Tempo_Troca");
		this.podeAndar = true;
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	public int getTempoTrocaCurrent() {
		return this.tempoTrocaCurrent;
	}

	public void setTempoTrocaCurrent(final int tempoTrocaCurrent) {
		this.tempoTrocaCurrent = tempoTrocaCurrent;
	}

	public boolean isPodeAndar() {
		return this.podeAndar;
	}

	public void setPodeAndar(final boolean podeAndar) {
		this.podeAndar = podeAndar;
	}

	public int getTempoTroca() {
		return this.tempoTroca;
	}

}
