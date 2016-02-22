package me.herobrinedobem.heventos.eventos;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.listeners.EventoNormalListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.EventoBase;

public class EventoNormal extends EventoBase {

	private final EventoNormalListener listener;

	public EventoNormal(final YamlConfiguration config) {
		super(config);
		this.listener = new EventoNormalListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(this.listener, HEventos.getHEventos());
	}

	@Override
	public void scheduledMethod() {
		if ((this.isOcorrendo() == true) && (this.isAberto() == false)) {
			if (this.getParticipantes().size() <= 0) {
				this.sendMessageList("Mensagens.Sem_Vencedor");
				this.resetEvent();
				HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId());
			} else if ((this.getParticipantes().size() == 1) && (this.getVencedores().size() == 1)) {
				if (this.isContarVitoria()) {
					if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
						HEventos.getHEventos().getMysql().addWinnerPoint(this.getVencedores().get(0));
					} else {
						HEventos.getHEventos().getSqlite().addWinnerPoint(this.getVencedores().get(0));
					}
				}
				this.stopEvent();
			} else if ((this.getParticipantes().size() == 2) && (this.getVencedores().size() == 2)) {
				if (this.isContarVitoria()) {
					if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
						HEventos.getHEventos().getMysql().addWinnerPoint(this.getVencedores().get(0));
					} else {
						HEventos.getHEventos().getSqlite().addWinnerPoint(this.getVencedores().get(0));
					}
				}
				if (this.isContarVitoria()) {
					if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
						HEventos.getHEventos().getMysql().addWinnerPoint(this.getVencedores().get(1));
					} else {
						HEventos.getHEventos().getSqlite().addWinnerPoint(this.getVencedores().get(1));
					}
				}
				this.stopEvent();
			} else if ((this.getParticipantes().size() == 2) && (this.getVencedores().size() == 2)) {
				if (this.isContarVitoria()) {
					if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
						HEventos.getHEventos().getMysql().addWinnerPoint(this.getVencedores().get(0));
					} else {
						HEventos.getHEventos().getSqlite().addWinnerPoint(this.getVencedores().get(0));
					}
				}
				if (this.isContarVitoria()) {
					if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
						HEventos.getHEventos().getMysql().addWinnerPoint(this.getVencedores().get(1));
					} else {
						HEventos.getHEventos().getSqlite().addWinnerPoint(this.getVencedores().get(1));
					}
				}
				if (this.isContarVitoria()) {
					if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
						HEventos.getHEventos().getMysql().addWinnerPoint(this.getVencedores().get(2));
					} else {
						HEventos.getHEventos().getSqlite().addWinnerPoint(this.getVencedores().get(2));
					}
				}
				this.stopEvent();
			}
		}
	}

	@Override
	public void stopEventMethod() {
		this.sendMessageListVencedor("Mensagens.Vencedor");
	}

	@Override
	public void cancelEventMethod() {
		this.sendMessageList("Mensagens.Cancelado");
	}

	public void removePlayerWinnerForEvent(final Player p, final int pos) {
		p.teleport(this.getSaida());
		this.getParticipantes().remove(p.getName());
		if (pos == 1) {
			this.getVencedores().add(0, p.getName());
			for (final String sa : this.getConfig().getStringList("Mensagens.Lugar")) {
				HEventos.getHEventos().getServer().broadcastMessage(sa.replace("&", "§").replace("$player$", p.getName()).replace("$posicao$", "1°"));
			}
			for (final String sa : this.getConfig().getStringList("Premios.Primeiro_Lugar")) {
				HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", this.getVencedores().get(0)));
			}

			final double money = this.getMoney() * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), "money give " + this.getVencedores().get(0) + " " + money);
			if (this.isContarVitoria()) {
				if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
					HEventos.getHEventos().getMysql().addWinnerPoint(this.getVencedores().get(0));
				} else {
					HEventos.getHEventos().getSqlite().addWinnerPoint(this.getVencedores().get(0));
				}
			}
		} else if (pos == 2) {
			this.getVencedores().add(1, p.getName());
			for (final String sa : this.getConfig().getStringList("Mensagens.Lugar")) {
				HEventos.getHEventos().getServer().broadcastMessage(sa.replace("&", "§").replace("$player$", p.getName()).replace("$posicao$", "2°"));
			}
			for (final String sa : this.getConfig().getStringList("Premios.Segundo_Lugar")) {
				HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", this.getVencedores().get(1)));
			}

			final double money = this.getMoney() * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), "money give " + this.getVencedores().get(1) + " " + money);
			if (this.isContarVitoria()) {
				if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
					HEventos.getHEventos().getMysql().addWinnerPoint(this.getVencedores().get(1));
				} else {
					HEventos.getHEventos().getSqlite().addWinnerPoint(this.getVencedores().get(1));
				}
			}
		} else if (pos == 3) {
			this.getVencedores().add(2, p.getName());
			for (final String sa : this.getConfig().getStringList("Mensagens.Lugar")) {
				HEventos.getHEventos().getServer().broadcastMessage(sa.replace("&", "§").replace("$player$", p.getName()).replace("$posicao$", "3°"));
			}
			for (final String sa : this.getConfig().getStringList("Premios.Terceiro_Lugar")) {
				HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", this.getVencedores().get(2)));
			}

			final double money = this.getMoney() * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), "money give " + this.getVencedores().get(2) + " " + money);
			if (this.isContarVitoria()) {
				if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
					HEventos.getHEventos().getMysql().addWinnerPoint(this.getVencedores().get(2));
				} else {
					HEventos.getHEventos().getSqlite().addWinnerPoint(this.getVencedores().get(2));
				}
			}
			this.stopEvent();
		}
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	private void sendMessageListVencedor(final String list) {
		for (final String s : this.getConfig().getStringList(list)) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", this.getVencedores().get(0)));
		}
	}

	public EventoNormalListener getListener() {
		return this.listener;
	}

}
