package me.herobrinedobem.heventos.eventos;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.listeners.KillerListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.EventoBase;

public class Killer extends EventoBase {

	private final KillerListener listener;
	private boolean msgTempo = false;
	private int tempoMensagens, tempoMensagensCurrent, tempoPegarItens,
			tempoPegarItensCurrent;

	public Killer(final YamlConfiguration config) {
		super(config);
		this.listener = new KillerListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(this.listener, HEventos.getHEventos());
		this.tempoPegarItens = config.getInt("Config.Tempo_Pegar_Itens");
		this.tempoPegarItensCurrent = config.getInt("Config.Tempo_Pegar_Itens");
		this.tempoMensagens = config.getInt("Config.Mensagens_Tempo_Minutos") * 60;
		this.tempoMensagensCurrent = this.tempoMensagens;
	}

	@Override
	public void startEventMethod() {
		Killer.this.getEntrada().getWorld().setTime(17000);
	}

	@Override
	public void scheduledMethod() {
		if ((Killer.this.isOcorrendo() == true) && (Killer.this.isAberto() == false)) {
			if (Killer.this.getParticipantes().size() > 1) {
				if (Killer.this.tempoMensagensCurrent == 0) {
					for (final String s : Killer.this.getConfig().getStringList("Mensagens.Status")) {
						HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$jogadores$", Killer.this.getParticipantes().size() + ""));
					}
					Killer.this.tempoMensagensCurrent = Killer.this.tempoMensagens;
				} else {
					Killer.this.tempoMensagensCurrent--;
				}
			} else {
				if (Killer.this.getParticipantes().size() == 1) {
					if (Killer.this.tempoPegarItensCurrent == 0) {
						final Player p = Killer.this.getPlayerByName(Killer.this.getParticipantes().get(0));
						Killer.this.encerrarComVencedor(p);
					} else {
						if (Killer.this.msgTempo == false) {
							for (final String s : Killer.this.getParticipantes()) {
								Killer.this.getPlayerByName(s).sendMessage(Killer.this.getConfig().getString("Mensagens.Tempo_Pegar_Itens").replace("&", "§"));
							}
							Killer.this.msgTempo = true;
						}
						Killer.this.tempoPegarItensCurrent--;
					}
				} else {
					Killer.this.encerrarSemVencedor();
				}
			}
		}
	}

	@Override
	public void cancelEventMethod() {
		this.sendMessageList("Mensagens.Cancelado");
	}

	public void encerrarComVencedor(final Player p) {
		for (final String s : this.getParticipantes()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.getParticipantes()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.getConfig().getStringList("Mensagens.Vencedor")) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", p.getName()));
		}
		for (final String s : this.getConfig().getStringList("Premios.Comandos")) {
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), s.replace("$player$", p.getName()));
		}
		if (Killer.this.isContarVitoria()) {
			if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
				HEventos.getHEventos().getMysql().addWinnerPoint(p.getName());
			} else {
				HEventos.getHEventos().getSqlite().addWinnerPoint(p.getName());
			}
		}
		HEventos.getHEventos().getEconomy().depositPlayer(p.getName(), this.getMoney());
		this.resetEvent();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId());
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId2());
	}

	public void encerrarSemVencedor() {
		for (final String s : this.getParticipantes()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.getCamarotePlayers()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.sendMessageList("Mensagens.Sem_Vencedor");
		this.resetEvent();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId());
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId2());
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		this.tempoPegarItens = this.getConfig().getInt("Config.Tempo_Pegar_Itens");
		this.tempoPegarItensCurrent = this.getConfig().getInt("Config.Tempo_Pegar_Itens");
		this.tempoMensagens = this.getConfig().getInt("Config.Mensagens_Tempo_Minutos") * 60;
		this.tempoMensagensCurrent = this.tempoMensagens;
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	public boolean isMsgTempo() {
		return this.msgTempo;
	}

	public void setMsgTempo(final boolean msgTempo) {
		this.msgTempo = msgTempo;
	}

	public int getTempoMensagens() {
		return this.tempoMensagens;
	}

	public void setTempoMensagens(final int tempoMensagens) {
		this.tempoMensagens = tempoMensagens;
	}

	public int getTempoMensagensCurrent() {
		return this.tempoMensagensCurrent;
	}

	public void setTempoMensagensCurrent(final int tempoMensagensCurrent) {
		this.tempoMensagensCurrent = tempoMensagensCurrent;
	}

	public int getTempoPegarItens() {
		return this.tempoPegarItens;
	}

	public void setTempoPegarItens(final int tempoPegarItens) {
		this.tempoPegarItens = tempoPegarItens;
	}

	public int getTempoPegarItensCurrent() {
		return this.tempoPegarItensCurrent;
	}

	public void setTempoPegarItensCurrent(final int tempoPegarItensCurrent) {
		this.tempoPegarItensCurrent = tempoPegarItensCurrent;
	}

	public KillerListener getListener() {
		return this.listener;
	}

}
