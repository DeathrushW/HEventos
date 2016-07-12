package me.herobrinedobem.heventos.eventos;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.listeners.MinaMortalListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.Cuboid;

public class MinaMortal extends EventoBase {

	private final MinaMortalListener listener;
	private int tempoDeEvento, tempoDeEventoCurrent, tempoMensagens,
			tempoMensagensCurrent;

	public MinaMortal(final YamlConfiguration config) {
		super(config);
		this.listener = new MinaMortalListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(this.listener, HEventos.getHEventos());
		this.tempoDeEvento = config.getInt("Config.Evento_Tempo_Minutos") * 60;
		this.tempoMensagens = config.getInt("Config.Mensagens_Tempo_Minutos") * 60;
		this.tempoDeEventoCurrent = this.tempoDeEvento;
		this.tempoMensagensCurrent = this.tempoMensagens;
	}

	@Override
	public void startEventMethod() {
		final Cuboid cubo = new Cuboid(MinaMortal.this.getLocation("Localizacoes.Mina_1"), MinaMortal.this.getLocation("Localizacoes.Mina_2"));
		final ArrayList<String> blocosConfig = new ArrayList<>();
		for (final String s : MinaMortal.this.getConfig().getString("Config.Minerios").split(";")) {
			blocosConfig.add(s);
		}
		for (final Block b : cubo.getBlocks()) {
			final Random r = new Random();
			if (r.nextInt(100) <= MinaMortal.this.getConfig().getInt("Config.Porcentagem_De_Minerios")) {
				final String bloco = blocosConfig.get(r.nextInt(blocosConfig.size()));
				b.setType(Material.getMaterial(Integer.parseInt(bloco)));
			} else {
				b.setType(Material.STONE);
			}
		}
	}

	@Override
	public void scheduledMethod() {
		if ((MinaMortal.this.isOcorrendo() == true) && (MinaMortal.this.isAberto() == false)) {
			if (MinaMortal.this.getParticipantes().size() > 0) {
				if (MinaMortal.this.tempoDeEventoCurrent > 0) {
					MinaMortal.this.tempoDeEventoCurrent--;
					if (MinaMortal.this.tempoMensagensCurrent == 0) {
						for (final String s : MinaMortal.this.getConfig().getStringList("Mensagens.Status")) {
							HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$tempo$", MinaMortal.this.tempoDeEventoCurrent + ""));
						}
						MinaMortal.this.tempoMensagensCurrent = MinaMortal.this.tempoMensagens;
					} else {
						MinaMortal.this.tempoMensagensCurrent--;
					}
				} else {
					MinaMortal.this.stopEvent();
				}
			} else {
				MinaMortal.this.stopEvent();
			}
		}
	}

	@Override
	public void stopEventMethod() {
		this.sendMessageList("Mensagens.Finalizado");
	}

	@Override
	public void cancelEventMethod() {
		this.sendMessageList("Mensagens.Cancelado");
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		this.tempoDeEvento = this.getConfig().getInt("Config.Evento_Tempo_Minutos") * 60;
		this.tempoMensagens = this.getConfig().getInt("Config.Mensagens_Tempo_Minutos") * 60;
		this.tempoDeEventoCurrent = this.tempoDeEvento;
		this.tempoMensagensCurrent = this.tempoMensagens;
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	public int getTempoDeEvento() {
		return this.tempoDeEvento;
	}

	public void setTempoDeEvento(final int tempoDeEvento) {
		this.tempoDeEvento = tempoDeEvento;
	}

	public int getTempoDeEventoCurrent() {
		return this.tempoDeEventoCurrent;
	}

	public void setTempoDeEventoCurrent(final int tempoDeEventoCurrent) {
		this.tempoDeEventoCurrent = tempoDeEventoCurrent;
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

	public MinaMortalListener getListener() {
		return this.listener;
	}

}
