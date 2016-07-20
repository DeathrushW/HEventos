package me.herobrinedobem.heventos.eventos;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;
import me.herobrinedobem.heventos.listeners.EventoNormalListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class EventoNormal extends EventoBase {

	private EventoNormalListener listener;

	public EventoNormal(YamlConfiguration config) {
		super(config);
		listener = new EventoNormalListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(listener, HEventos.getHEventos());
	}
	
	@Override
	public void startEventMethod() {
		for (String s : getParticipantes()) {
			getPlayerByName(s).teleport(HEventosAPI.getLocation(getConfig(), "Localizacoes.Entrada"));
		}
	}

	@Override
	public void cancelEventMethod() {
		sendMessageList("Mensagens.Cancelado");
	}

	public void removePlayerWinnerForEvent(Player p, int pos) {
		if (pos == 1) {
			getVencedores().add(0, p.getName());
			EventoPlayerWinEvent event = new EventoPlayerWinEvent(p, this, 1);
			HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
		} else if (pos == 2) {
			getVencedores().add(1, p.getName());
			EventoPlayerWinEvent event = new EventoPlayerWinEvent(p, this, 2);
			HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
		} else if (pos == 3) {
			getVencedores().add(2, p.getName());
			EventoPlayerWinEvent event = new EventoPlayerWinEvent(p, this, 3);
			HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
			stopEvent();
		}
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		BukkitEventHelper.unregisterEvents(listener, HEventos.getHEventos());
	}

	public EventoNormalListener getListener() {
		return this.listener;
	}

}
