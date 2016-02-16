package me.herobrinedobem.heventos.listeners;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.eventos.EventoNormal;

public class EventoNormalListener implements Listener {

	private final EventoNormal eventoNormal;

	public EventoNormalListener(final EventoNormal eventoNormal) {
		this.eventoNormal = eventoNormal;
	}

	@EventHandler
	private void onPlayerInteractEvent(final PlayerInteractEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
				if (Action.RIGHT_CLICK_BLOCK == e.getAction()) {
					if ((e.getClickedBlock().getType() == Material.SIGN_POST) || (e.getClickedBlock().getType() == Material.WALL_SIGN)) {
						final Sign s = (Sign) e.getClickedBlock().getState();
						if (s.getLine(0).equalsIgnoreCase("§9[Evento]")) {

							if (HEventos.getHEventos().getEventosController().getEvento().getVencedores().size() == 0) {
								if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().size() == 1) {
									this.eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 1);
									s.setLine(1, "§6" + e.getPlayer().getName());
									s.update();
									this.eventoNormal.stopEvent();
								} else {
									this.eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 1);
									s.setLine(1, "§6" + e.getPlayer().getName());
									s.update();
								}
							} else if (HEventos.getHEventos().getEventosController().getEvento().getVencedores().size() == 1) {
								if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().size() == 2) {
									if (!HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(0).equalsIgnoreCase(e.getPlayer().getName())) {
										this.eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 2);
										s.setLine(2, "§6" + e.getPlayer().getName());
										s.update();
										this.eventoNormal.stopEvent();
									}
								} else {
									this.eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 2);
									s.setLine(2, "§6" + e.getPlayer().getName());
									s.update();
								}
							} else if (HEventos.getHEventos().getEventosController().getEvento().getVencedores().size() == 2) {
								if (!HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(0).equalsIgnoreCase(e.getPlayer().getName()) && !HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(1).equalsIgnoreCase(e.getPlayer().getName())) {
									if (!HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(0).equalsIgnoreCase(e.getPlayer().getName())) {
										if (!HEventos.getHEventos().getEventosController().getEvento().getVencedores().get(1).equalsIgnoreCase(e.getPlayer().getName())) {
											this.eventoNormal.removePlayerWinnerForEvent(e.getPlayer(), 3);
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

	@EventHandler
	private void onSignChangeEvent(final SignChangeEvent e) {
		if (e.getPlayer().hasPermission("heventos.admin")) {
			if (e.getLine(0).equalsIgnoreCase("[Evento]")) {
				e.setLine(0, "§9[Evento]");
				e.setLine(1, "§61 Lugar");
				e.setLine(2, "§62 Lugar");
				e.setLine(3, "§63 Lugar");
				e.getPlayer().sendMessage("§4[Evento] §cPlaca criada com sucesso!");
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (e.getDamager() instanceof Player) {
				final Player p = (Player) e.getDamager();
				if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
					if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
				if (!HEventos.getHEventos().getEventosController().getEvento().isPvp()) {
					if (HEventos.getHEventos().getEventosController().getEvento().getVencedores().contains(p.getName())) {
						e.setCancelled(true);
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
				if (!HEventos.getHEventos().getEventosController().getEvento().isPvp()) {
					if (HEventos.getHEventos().getEventosController().getEvento().getVencedores().contains(p.getName())) {
						e.setCancelled(true);
					}
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
