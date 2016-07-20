package me.herobrinedobem.heventos.listeners;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBaseListener;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerLoseEvent;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;
import me.herobrinedobem.heventos.eventos.Semaforo;

public class SemaforoListener extends EventoBaseListener {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
				if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
					if (Action.RIGHT_CLICK_BLOCK == e.getAction()) {
						if ((e.getClickedBlock().getType() == Material.SIGN_POST) || (e.getClickedBlock().getType() == Material.WALL_SIGN)) {
							Sign s = (Sign) e.getClickedBlock().getState();
							if (s.getLine(0).equalsIgnoreCase("§9[Evento]")) {
								EventoPlayerWinEvent event = new EventoPlayerWinEvent(e.getPlayer(), HEventos.getHEventos().getEventosController().getEvento(), 0);
								HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
								HEventos.getHEventos().getEventosController().getEvento().stopEvent();
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
				if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
					if ((e.getFrom().getX() != e.getTo().getX()) && (e.getFrom().getZ() != e.getTo().getZ())) {
						Semaforo semaforo = (Semaforo) HEventos.getHEventos().getEventosController().getEvento();
						if (semaforo.isPodeAndar() == false) {
							for (String sa : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Eliminado")) {
								for (String pa : semaforo.getParticipantes()) {
									semaforo.getPlayerByName(pa).sendMessage(sa.replace("$player$", e.getPlayer().getName()).replace("&", "§"));
								}
							}
							EventoPlayerLoseEvent event = new EventoPlayerLoseEvent(e.getPlayer(), HEventos.getHEventos().getEventosController().getEvento());
							HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
						}
					}
				}
			}
		}
	}

}
