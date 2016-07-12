package me.herobrinedobem.heventos.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBaseListener;
import me.herobrinedobem.heventos.eventos.Spleef;

public class SpleefListener extends EventoBaseListener {

	@Override
	public void onBlockBreakEvent(final BlockBreakEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(p.getName())) {
				final Spleef spleef = (Spleef) HEventos.getHEventos().getEventosController().getEvento();
				if (spleef.isPodeQuebrar() == false) {
					for (final String sa : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Aguarde_Quebrar")) {
						e.getPlayer().sendMessage(sa.replace("&", "§").replace("$tempo$", spleef.getTempoComecarCurrent() + ""));
					}
					e.setCancelled(true);
				}
			}
			if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(final PlayerMoveEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(p.getName())) {
				final Spleef spleef = (Spleef) HEventos.getHEventos().getEventosController().getEvento();
				if ((p.getLocation().getY() <= spleef.getY()) && (HEventos.getHEventos().getEventosController().getEvento().isAberto() == false)) {
					e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
					for (final String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
						final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
						for (final String sa : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Eliminado")) {
							pa.sendMessage(sa.replace("&", "§").replace("$player$", e.getPlayer().getName()));
						}
					}
					HEventos.getHEventos().getEventosController().getEvento().getParticipantes().remove(e.getPlayer().getName());
				}
			}
		}
	}

}
