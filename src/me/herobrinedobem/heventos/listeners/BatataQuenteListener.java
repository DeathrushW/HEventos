package me.herobrinedobem.heventos.listeners;

import java.util.Random;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.eventos.BatataQuente;

public class BatataQuenteListener implements Listener {

	private final BatataQuente batataQuente;

	public BatataQuenteListener(final BatataQuente batataQuente) {
		this.batataQuente = batataQuente;
	}

	@EventHandler
	private void onPlayerInteractEvent(final PlayerInteractEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (e.getRightClicked() instanceof Player) {
				final Player pa = (Player) e.getRightClicked();
				if (HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) {
					if (e.getPlayer().getName().equalsIgnoreCase(this.batataQuente.getPlayerComBatata().getName())) {
						if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(pa.getName())) {
							this.batataQuente.setPlayerComBatata((Player) e.getRightClicked());
							for (final String s : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Esta_Com_Batata")) {
								for (final String sa : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
									final Player p = HEventos.getHEventos().getServer().getPlayer(sa);
									p.sendMessage(s.replace("&", "§").replace("$player$", this.batataQuente.getPlayerComBatata().getName()));
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (e.getDamager() instanceof Player) {
				final Player p = (Player) e.getDamager();
				if (HEventos.getHEventos().getEventosController().getEvento().isPvp() == false) {
					if (HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) {
						if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(p.getName())) {
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
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (e.getEntity() instanceof Player) {
				final Player p = (Player) e.getEntity();
				if (HEventos.getHEventos().getEventosController().getEvento().isPvp() == false) {
					if (HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) {
						if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(p.getName())) {
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
		}
	}

	@EventHandler
	private void onPlayerPickupEvent(final PlayerPickupItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
				final Player p = e.getPlayer();
				if (HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) {
					if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDeathEvent(final PlayerDeathEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) {
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
				if (this.batataQuente.getPlayerComBatata() == e.getPlayer()) {
					final Random r = new Random();
					this.batataQuente.setPlayerComBatata(this.batataQuente.getPlayerByName(HEventos.getHEventos().getEventosController().getEvento().getParticipantes().get(r.nextInt(HEventos.getHEventos().getEventosController().getEvento().getParticipantes().size()))));
					for (final String s : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Esta_Com_Batata")) {
						for (final String sa : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
							final Player sab = this.batataQuente.getPlayerByName(sa);
							sab.sendMessage(s.replace("&", "§").replace("$player$", this.batataQuente.getPlayerComBatata().getName()));
						}
					}
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
				if (HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) {
					if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(e.getPlayer().getName())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	private void onBlockBreakEvent(final BlockBreakEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
				final Player p = e.getPlayer();
				if (HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) {
					if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

}
