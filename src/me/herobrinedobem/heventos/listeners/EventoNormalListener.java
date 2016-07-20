package me.herobrinedobem.heventos.listeners;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBaseListener;
import me.herobrinedobem.heventos.eventos.EventoNormal;

public class EventoNormalListener extends EventoBaseListener {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
				if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
					if (Action.RIGHT_CLICK_BLOCK == e.getAction()) {
						if ((e.getClickedBlock().getType() == Material.SIGN_POST) || (e.getClickedBlock().getType() == Material.WALL_SIGN)) {
							if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
								Sign s = (Sign) e.getClickedBlock().getState();
								if (s.getLine(0).equalsIgnoreCase("§9[Evento]")) {
									EventoNormal eventoNormal = (EventoNormal) HEventos.getHEventos().getEventosController().getEvento();
									if (HEventos.getHEventos().getEventosController().getEvento().getVencedores().size() == 0) {
										if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().size() == 1) {
											eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 1);
											s.setLine(1, "§6" + e.getPlayer().getName());
											s.update();
											eventoNormal.stopEvent();
										} else {
											eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 1);
											s.setLine(1, "§6" + e.getPlayer().getName());
											s.update();
										}
									} else if (HEventos.getHEventos().getEventosController().getEvento().getVencedores().size() == 1) {
										if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().size() == 2) {
											if (!HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(0).equalsIgnoreCase(e.getPlayer().getName())) {
												eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 2);
												s.setLine(2, "§6" + e.getPlayer().getName());
												s.update();
												eventoNormal.stopEvent();
											}
										} else {
											eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 2);
											s.setLine(2, "§6" + e.getPlayer().getName());
											s.update();
										}
									} else if (HEventos.getHEventos().getEventosController().getEvento().getVencedores().size() == 2) {
										if (!HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(0).equalsIgnoreCase(e.getPlayer().getName()) && !HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(1).equalsIgnoreCase(e.getPlayer().getName())) {
											if (!HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(0).equalsIgnoreCase(e.getPlayer().getName())) {
												if (!HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(1).equalsIgnoreCase(e.getPlayer().getName())) {
													eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 3);
													s.setLine(3, "§6" + e.getPlayer().getName());
													s.update();
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
