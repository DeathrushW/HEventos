package me.herobrinedobem.heventos.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.eventos.Paintball;

public class PaintballListener implements Listener {

	private final Paintball paintball;

	public PaintballListener(final Paintball paintball) {
		this.paintball = paintball;
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
				if (e.getDamager() instanceof Player) {
					final Player p = (Player) e.getDamager();
					if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
						if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
					if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
				} else if (e.getDamager() instanceof Arrow) {
					final Arrow projectile = (Arrow) e.getDamager();
					if (projectile.getShooter() instanceof Player) {
						final Player atirou = (Player) projectile.getShooter();
						final Player atingido = (Player) e.getEntity();
						atingido.setHealth(20);
						boolean matou = false;
						if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(atingido.getName()) && HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(atirou.getName())) {
							if (this.paintball.getTeam1().contains(atirou.getName())) {
								if (this.paintball.getTeam1().contains(atingido.getName())) {
									matou = false;
								} else {
									matou = true;
								}
							} else if (this.paintball.getTeam2().contains(atirou.getName())) {
								if (this.paintball.getTeam2().contains(atingido.getName())) {
									matou = false;
								} else {
									matou = true;
								}
							}
							if (matou) {
								if (this.paintball.getTeam2().contains(atingido.getName())) {
									this.paintball.getTeam2().remove(atingido.getName());
								} else {
									this.paintball.getTeam1().remove(atingido.getName());
								}
								atingido.getInventory().setHelmet(null);
								atingido.getInventory().setChestplate(null);
								atingido.getInventory().setLeggings(null);
								atingido.getInventory().setBoots(null);
								atingido.getInventory().clear();
								atingido.teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
								HEventos.getHEventos().getEventosController().getEvento().getParticipantes().remove(atingido.getName());
								atingido.sendMessage(HEventos.getHEventos().getEventosController().getEvento().getConfig().getString("Mensagens.Eliminado").replace("&", "§").replace("$player$", atirou.getName()));
								atirou.sendMessage(HEventos.getHEventos().getEventosController().getEvento().getConfig().getString("Mensagens.Eliminou").replace("&", "§").replace("$player$", atingido.getName()));
							} else {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (e.getEntity() instanceof Player) {
				final Player p = (Player) e.getEntity();
				if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
					if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
				if (HEventos.getHEventos().getEventosController().getEvento().getVencedores().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDeathEvent(final PlayerDeathEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getEntity().getPlayer().getName())) {
				for (final String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgMorreu().replace("$player$", e.getEntity().getPlayer().getName()));
				}
				HEventos.getHEventos().getEventosController().getEvento().getParticipantes().remove(e.getEntity().getPlayer().getName());
				e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
				e.setNewTotalExp(e.getDroppedExp());
			}
			if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(e.getEntity().getPlayer().getName())) {
					HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().remove(e.getEntity().getPlayer().getName());
					e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
					e.setNewTotalExp(e.getDroppedExp());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerQuitEvent(final PlayerQuitEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
				HEventos.getHEventos().getEventosController().getEvento().getParticipantes().remove(e.getPlayer().getName());
				e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
				e.getPlayer().getInventory().setHelmet(null);
				e.getPlayer().getInventory().setChestplate(null);
				e.getPlayer().getInventory().setLeggings(null);
				e.getPlayer().getInventory().setBoots(null);
				e.getPlayer().getInventory().clear();
				for (final String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgDesconect().replace("$player$", e.getPlayer().getName()));
				}
			}
			if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(e.getPlayer().getName())) {
					HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().remove(e.getPlayer().getName());
					e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerProccessCommandEvent(final PlayerCommandPreprocessEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName()) || HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(e.getPlayer().getName())) {
				if (!e.getPlayer().hasPermission("heventos.admin")) {
					for (final String s : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Comandos_Liberados")) {
						if (!e.getMessage().startsWith(s)) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDropEvent(final PlayerDropItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerPickupItemEvent(final PlayerPickupItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

}
