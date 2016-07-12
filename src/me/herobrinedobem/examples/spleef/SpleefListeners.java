package me.herobrinedobem.examples.spleef;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBaseListener;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.eventos.Spleef;

public class SpleefListeners extends EventoBaseListener{
	
	@Override
	public void onBlockBreakEvent(final BlockBreakEvent e) {
		if (HEventosAPI.getEventoOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventosAPI.getEventoOcorrendo().getParticipantes().contains(p.getName())) {
				final Spleef spleef = (Spleef) HEventosAPI.getEventoOcorrendo();
				if (spleef.isPodeQuebrar() == false) {
					for (final String sa : HEventosAPI.getEventoOcorrendo().getConfig().getStringList("Mensagens.Aguarde_Quebrar")) {
						e.getPlayer().sendMessage(sa.replace("&", "§").replace("$tempo$", spleef.getTempoComecarCurrent() + ""));
					}
					e.setCancelled(true);
				}
			}
			if (HEventosAPI.getEventoOcorrendo().isAssistirAtivado()) {
				if (HEventosAPI.getEventoOcorrendo().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(final PlayerMoveEvent e) {
		if (HEventosAPI.getEventoOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventosAPI.getEventoOcorrendo().getParticipantes().contains(p.getName())) {
				final Spleef spleef = (Spleef) HEventosAPI.getEventoOcorrendo();
				if ((p.getLocation().getY() <= spleef.getY()) && (HEventosAPI.getEventoOcorrendo().isAberto() == false)) {
					e.getPlayer().teleport(HEventosAPI.getEventoOcorrendo().getSaida());
					for (final String s : HEventosAPI.getEventoOcorrendo().getParticipantes()) {
						final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
						for (final String sa : HEventosAPI.getEventoOcorrendo().getConfig().getStringList("Mensagens.Eliminado")) {
							pa.sendMessage(sa.replace("&", "§").replace("$player$", e.getPlayer().getName()));
						}
					}
					HEventosAPI.getEventoOcorrendo().getParticipantes().remove(e.getPlayer().getName());
				}
			}
		}
	}
	
}
