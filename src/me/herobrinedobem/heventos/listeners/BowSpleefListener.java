package me.herobrinedobem.heventos.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBaseListener;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerLoseEvent;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;
import me.herobrinedobem.heventos.eventos.BowSpleef;

public class BowSpleefListener extends EventoBaseListener {

	@EventHandler
	private void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento() instanceof BowSpleef) {
				BowSpleef bows = (BowSpleef) HEventos.getHEventos().getEventosController().getEvento();
				if (bows.getParticipantes().contains(e.getPlayer().getName())) {
					if (bows.isAberto() == false) {
						if (e.getFrom() != e.getTo()) {
							if (e.getTo().getY() < (bows.getChao().getLowerLocation().getY() - 2)) {
								for(String p : bows.getParticipantes()){
									for (String s : bows.getConfig().getStringList("Mensagens.Eliminado")) {
										bows.getPlayerByName(p).sendMessage(s.replace("&", "§").replace("$player$", e.getPlayer().getName()));
									}
								}
								EventoPlayerLoseEvent event = new EventoPlayerLoseEvent(e.getPlayer(), HEventos.getHEventos().getEventosController().getEvento());
								HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
								if (bows.getParticipantes().size() == 1) {
									EventoPlayerWinEvent event2 = new EventoPlayerWinEvent(bows.getPlayerByName(bows.getParticipantes().get(0)), HEventos.getHEventos().getEventosController().getEvento(), 0);
									HEventos.getHEventos().getServer().getPluginManager().callEvent(event2);
									bows.stopEvent();
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
				BowSpleef bows = (BowSpleef) HEventos.getHEventos().getEventosController().getEvento();
				if (e.getDamager() instanceof Player) {
					Player p = (Player) e.getDamager();
					if (bows.isAssistirAtivado()) {
						if (bows.getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
					if (bows.getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
				} else if (e.getDamager() instanceof Arrow) {
					Arrow projectile = (Arrow) e.getDamager();
					if (projectile.getShooter() instanceof Block) {
						Player atirou = (Player) projectile.getShooter();
						Block atingido = (Block) e.getEntity();
						if (atingido.getType() == Material.TNT) {
							if (bows.isPodeQuebrar() == false) {
								for (String s : bows.getConfig().getStringList("Mensagens.Aguarde_Quebrar")) {
									atirou.sendMessage(s.replace("&", "§").replace("$tempo$", bows.getTempoInicial() + ""));
								}
							}
						}
					}
				}
			}
		}
	}

}
