package me.herobrinedobem.heventos.api;

import java.util.ArrayList;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.listeners.EventoStopEvent;
import me.herobrinedobem.heventos.utils.EventoCancellType;

public class EventoBase implements EventoBaseImplements {

	private final EventoType eventoType;
	private String horarioStart;
	private final ArrayList<String> participantes = new ArrayList<String>();
	private boolean ocorrendo, aberto, parte1, vip, assistirAtivado,
			assistirInvisivel, pvp, contarVitoria, contarParticipacao;
	private int chamadas, tempo, id2, chamadascurrent, id;
	private double money;
	private String nome;
	private Location saida, entrada, camarote, aguarde;
	private final ArrayList<String> vencedores = new ArrayList<String>();
	private final ArrayList<String> camarotePlayers = new ArrayList<String>();
	private YamlConfiguration config;

	public EventoBase(final YamlConfiguration config) {
		this.config = config;
		this.eventoType = EventoType.getEventoType(config.getString("Config.Evento_Type"));
		this.nome = this.config.getString("Config.Nome");
		this.chamadas = this.config.getInt("Config.Chamadas");
		if (this.vip == false) {
			this.vip = this.config.getBoolean("Config.VIP");
		}
		this.assistirAtivado = this.config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = this.config.getBoolean("Config.Assistir_Invisivel");
		this.pvp = this.config.getBoolean("Config.PVP");
		this.contarParticipacao = this.config.getBoolean("Config.Contar_Participacao");
		this.contarVitoria = this.config.getBoolean("Config.Contar_Vitoria");
		this.tempo = this.config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.money = this.config.getInt("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.aberto = false;
		this.ocorrendo = false;
		this.parte1 = false;
		this.participantes.clear();
		this.chamadascurrent = this.chamadas;
	}

	public void run() {
		final BukkitScheduler scheduler = HEventos.getHEventos().getServer().getScheduler();
		this.id = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if (!EventoBase.this.parte1) {
					EventoBase.this.startEvent();
				}
			}
		}, 0, this.tempo * 20L);

		this.id2 = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if (EventoBase.this.isAssistirInvisivel()) {
					for (final String s : EventoBase.this.participantes) {
						for (final String sa : EventoBase.this.camarotePlayers) {
							EventoBase.this.getPlayerByName(s).hidePlayer(EventoBase.this.getPlayerByName(sa));
						}
					}
				}
				EventoBase.this.scheduledMethod();
			}
		}, 0, 20L);
	}

	@Override
	public void cancelEvent() {
		for (final String s : this.participantes) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.cancelEventMethod();
		this.resetEvent();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
	}

	@Override
	public void cancelEventMethod() {
	}

	@Override
	public void startEvent() {
		if (EventoBase.this.chamadascurrent >= 1) {
			EventoBase.this.chamadascurrent--;
			EventoBase.this.ocorrendo = true;
			EventoBase.this.aberto = true;
			if (EventoBase.this.vip) {
				EventoBase.this.sendMessageList("Mensagens.Aberto_VIP");
			} else {
				EventoBase.this.sendMessageList("Mensagens.Aberto");
			}
		} else if (EventoBase.this.chamadascurrent == 0) {
			if (EventoBase.this.participantes.size() >= 1) {
				EventoBase.this.aberto = false;
				EventoBase.this.parte1 = true;
				this.startEventMethod();
				EventoBase.this.sendMessageList("Mensagens.Iniciando");
				for (final String sa : EventoBase.this.camarotePlayers) {
					EventoBase.this.getPlayerByName(sa).teleport(EventoBase.this.camarote);
				}
				for (final String p : EventoBase.this.participantes) {
					EventoBase.this.getPlayerByName(p).teleport(EventoBase.this.entrada);
					if (EventoBase.this.contarParticipacao) {
						if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
							HEventos.getHEventos().getMysql().addPartipationPoint(p);
						} else {
							HEventos.getHEventos().getSqlite().addPartipationPoint(p);
						}
					}
				}
			} else {
				EventoBase.this.resetEvent();
				EventoBase.this.sendMessageList("Mensagens.Cancelado");
				HEventos.getHEventos().getServer().getScheduler().cancelTask(EventoBase.this.id);
			}
		}
	}

	@Override
	public void startEventMethod() {
	}

	@Override
	public void scheduledMethod() {
	}
	
	@Override
	public void externalPluginStart() {
		if(HEventos.getHEventos().getEventosController().getEvento() == null){
			HEventos.getHEventos().getEventosController().setEvento(this);
			HEventos.getHEventos().getEventosController().getEvento().setVip(isVip());
			HEventos.getHEventos().getEventosController().getEvento().run();
		}
	}

	@Override
	public void stopEvent() {
		EventoStopEvent event = new EventoStopEvent(HEventos.getHEventos().getEventosController().getEvento(), EventoCancellType.FINISHED);
		HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
		for (final String s : this.participantes) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.stopEventMethod();
		this.resetEvent();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
	}

	@Override
	public void stopEventMethod() {
	}

	@Override
	public void resetEvent() {
		this.nome = this.config.getString("Config.Nome");
		this.chamadas = this.config.getInt("Config.Chamadas");
		this.vip = this.config.getBoolean("Config.VIP");
		this.assistirAtivado = this.config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = this.config.getBoolean("Config.Assistir_Invisivel");
		this.pvp = this.config.getBoolean("Config.PVP");
		this.contarParticipacao = this.config.getBoolean("Config.Contar_Participacao");
		this.contarVitoria = this.config.getBoolean("Config.Contar_Vitoria");
		this.tempo = this.config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.money = this.config.getInt("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.aberto = false;
		this.ocorrendo = false;
		this.parte1 = false;
		for (final String s : this.participantes) {
			this.getPlayerByName(s).setGameMode(GameMode.SURVIVAL);
		}
		this.participantes.clear();
		this.chamadascurrent = this.chamadas;
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
			this.getPlayerByName(s).setAllowFlight(false);
			this.getPlayerByName(s).setFlying(false);
		}
		for (final Player s : HEventos.getHEventos().getServer().getOnlinePlayers()) {
			for (final String sa : this.camarotePlayers) {
				s.showPlayer(this.getPlayerByName(sa));
			}
		}
		this.camarotePlayers.clear();
		HEventos.getHEventos().getEventosController().setEvento(null);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
	}

	public Player getPlayerByName(final String name) {
		return HEventos.getHEventos().getServer().getPlayer(name);
	}

	public void sendMessageList(final String list) {
		for (final String s : this.config.getStringList(list)) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§"));
		}
	}

	public Location getLocation(final String local) {
		final String world = this.config.getString(local).split(";")[0];
		final double x = Double.parseDouble(this.config.getString(local).split(";")[1]);
		final double y = Double.parseDouble(this.config.getString(local).split(";")[2]);
		final double z = Double.parseDouble(this.config.getString(local).split(";")[3]);
		return new Location(HEventos.getHEventos().getServer().getWorld(world), x, y, z);
	}

	public int getId() {
		return this.id;
	}

	public EventoType getEventoType() {
		return this.eventoType;
	}

	public boolean isOcorrendo() {
		return this.ocorrendo;
	}

	public void setOcorrendo(final boolean ocorrendo) {
		this.ocorrendo = ocorrendo;
	}

	public boolean isAberto() {
		return this.aberto;
	}

	public void setAberto(final boolean aberto) {
		this.aberto = aberto;
	}

	public boolean isParte1() {
		return this.parte1;
	}

	public void setParte1(final boolean parte1) {
		this.parte1 = parte1;
	}

	public boolean isVip() {
		return this.vip;
	}

	public void setVip(final boolean vip) {
		this.vip = vip;
	}

	public boolean isAssistirAtivado() {
		return this.assistirAtivado;
	}

	public void setAssistirAtivado(final boolean assistirAtivado) {
		this.assistirAtivado = assistirAtivado;
	}

	public boolean isAssistirInvisivel() {
		return this.assistirInvisivel;
	}

	public void setAssistirInvisivel(final boolean assistirInvisivel) {
		this.assistirInvisivel = assistirInvisivel;
	}

	public boolean isPvp() {
		return this.pvp;
	}

	public void setPvp(final boolean pvp) {
		this.pvp = pvp;
	}

	public boolean isContarVitoria() {
		return this.contarVitoria;
	}

	public void setContarVitoria(final boolean contarVitoria) {
		this.contarVitoria = contarVitoria;
	}

	public boolean isContarParticipacao() {
		return this.contarParticipacao;
	}

	public void setContarParticipacao(final boolean contarParticipacao) {
		this.contarParticipacao = contarParticipacao;
	}

	public int getChamadas() {
		return this.chamadas;
	}

	public void setChamadas(final int chamadas) {
		this.chamadas = chamadas;
	}

	public int getTempo() {
		return this.tempo;
	}

	public void setTempo(final int tempo) {
		this.tempo = tempo;
	}

	public void setId(final int id) {
	}

	public int getId2() {
		return this.id2;
	}

	public void setId2(final int id2) {
		this.id2 = id2;
	}

	public int getChamadascurrent() {
		return this.chamadascurrent;
	}

	public void setChamadascurrent(final int chamadascurrent) {
		this.chamadascurrent = chamadascurrent;
	}

	public double getMoney() {
		return this.money;
	}

	public void setMoney(final double money) {
		this.money = money;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Location getSaida() {
		return this.saida;
	}

	public void setSaida(final Location saida) {
		this.saida = saida;
	}

	public Location getEntrada() {
		return this.entrada;
	}

	public void setEntrada(final Location entrada) {
		this.entrada = entrada;
	}

	public Location getCamarote() {
		return this.camarote;
	}

	public void setCamarote(final Location camarote) {
		this.camarote = camarote;
	}

	public Location getAguarde() {
		return this.aguarde;
	}

	public void setAguarde(final Location aguarde) {
		this.aguarde = aguarde;
	}

	public YamlConfiguration getConfig() {
		return this.config;
	}

	public void setConfig(final YamlConfiguration config) {
		this.config = config;
	}

	public ArrayList<String> getParticipantes() {
		return this.participantes;
	}

	public ArrayList<String> getVencedores() {
		return this.vencedores;
	}

	public ArrayList<String> getCamarotePlayers() {
		return this.camarotePlayers;
	}

	public String getHorarioStart() {
		return horarioStart;
	}
	
	public void setHorarioStart(String horarioStart) {
		this.horarioStart = horarioStart;
	}
	
}
