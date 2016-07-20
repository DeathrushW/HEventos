package me.herobrinedobem.heventos.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBaseListener;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerLoseEvent;
import me.herobrinedobem.heventos.eventos.Spleef;

public class SpleefListener extends EventoBaseListener {

	@Override
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
				if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
					Spleef spleef = (Spleef) HEventos.getHEventos().getEventosController().getEvento();
					if (spleef.isPodeQuebrar() == false) {
						for (String sa : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Aguarde_Quebrar")) {
							e.getPlayer().sendMessage(sa.replace("&", "§").replace("$tempo$", spleef.getTempoComecarCurrent() + ""));
						}
						e.setCancelled(true);
					}
				}
				if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
					if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(e.getPlayer().getName())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
				if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
					Spleef spleef = (Spleef) HEventos.getHEventos().getEventosController().getEvento();
					if ((e.getPlayer().getLocation().getY() <= spleef.getY())){
						for (String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
							Player pa = HEventos.getHEventos().getServer().getPlayer(s);
							for (String sa : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Eliminado")) {
								pa.sendMessage(sa.replace("&", "§").replace("$player$", e.getPlayer().getName()));
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
