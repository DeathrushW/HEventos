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
import me.herobrinedobem.heventos.eventos.BowSpleef;

public class BowSpleefListener extends EventoBaseListener {

	@SuppressWarnings("deprecation")
	@EventHandler
	private void onPlayerMoveEvent(final PlayerMoveEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento() instanceof BowSpleef) {
				final BowSpleef bows = (BowSpleef) HEventos.getHEventos().getEventosController().getEvento();
				if (bows.getParticipantes().contains(e.getPlayer().getName())) {
					if (bows.isAberto() == false) {
						if (e.getFrom() != e.getTo()) {
							if (e.getTo().getY() < (bows.getChao1().getY() - 10)) {
								for (final String s : bows.getConfig().getStringList("Mensagens.Eliminado")) {
									HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", e.getPlayer().getName()));
								}
								e.getPlayer().getInventory().clear();
								e.getPlayer().updateInventory();
								bows.getParticipantes().remove(e.getPlayer().getName());
								e.getPlayer().teleport(bows.getSaida());
								if (bows.getParticipantes().size() == 1) {
									for (final String sa : bows.getConfig().getStringList("Mensagens.Vencedor")) {
										for (final String s : bows.getParticipantes()) {
											bows.getPlayerByName(s).sendMessage(sa.replace("$player$", bows.getPlayerByName(bows.getParticipantes().get(0)).getName()).replace("&", "§"));
										}
									}
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
	public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
				final BowSpleef bows = (BowSpleef) HEventos.getHEventos().getEventosController().getEvento();
				if (e.getDamager() instanceof Player) {
					final Player p = (Player) e.getDamager();
					if (bows.isAssistirAtivado()) {
						if (bows.getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
					if (bows.getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
				} else if (e.getDamager() instanceof Arrow) {
					final Arrow projectile = (Arrow) e.getDamager();
					if (projectile.getShooter() instanceof Block) {
						final Player atirou = (Player) projectile.getShooter();
						final Block atingido = (Block) e.getEntity();
						if (atingido.getType() == Material.TNT) {
							if (bows.isPodeQuebrar() == false) {
								for (final String s : bows.getConfig().getStringList("Mensagens.Aguarde_Quebrar")) {
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
