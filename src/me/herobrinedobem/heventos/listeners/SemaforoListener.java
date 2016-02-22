package me.herobrinedobem.heventos.listeners;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.eventos.Semaforo;
import me.herobrinedobem.heventos.utils.EventoBaseListener;

public class SemaforoListener extends EventoBaseListener {

	@EventHandler
	public void onPlayerInteractEvent(final PlayerInteractEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
				if (Action.RIGHT_CLICK_BLOCK == e.getAction()) {
					if ((e.getClickedBlock().getType() == Material.SIGN_POST) || (e.getClickedBlock().getType() == Material.WALL_SIGN)) {
						final Sign s = (Sign) e.getClickedBlock().getState();
						if (s.getLine(0).equalsIgnoreCase("§9[Evento]")) {
							for (final String sa : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Vencedor")) {
								HEventos.getHEventos().getServer().broadcastMessage(sa.replace("&", "§").replace("$player$", e.getPlayer().getName()));
							}
							HEventos.getHEventos().getEconomy().depositPlayer(e.getPlayer().getName(), HEventos.getHEventos().getEventosController().getEvento().getMoney());
							HEventos.getHEventos().getEventosController().getEvento().stopEvent();
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerMoveEvent(final PlayerMoveEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
				if ((e.getFrom().getX() != e.getTo().getX()) && (e.getFrom().getZ() != e.getTo().getZ())) {
					final Semaforo semaforo = (Semaforo) HEventos.getHEventos().getEventosController().getEvento();
					if (semaforo.isPodeAndar() == false) {
						for (final String sa : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Eliminado")) {
							for (final String pa : semaforo.getParticipantes()) {
								semaforo.getPlayerByName(pa).sendMessage(sa.replace("$player$", e.getPlayer().getName()).replace("&", "§"));
							}
						}
						HEventos.getHEventos().getEventosController().getEvento().getParticipantes().remove(e.getPlayer().getName());
						e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
					}
				}
			}
		}
	}

}
