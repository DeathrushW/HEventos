package me.herobrinedobem.heventos.api;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.herobrinedobem.heventos.HEventos;

public class EventoBaseListener implements Listener {

	@EventHandler
	public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent e) {
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
	public void onEntityDamageEventEvent(final EntityDamageEvent e) {
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
	public void onPlayerDeathEvent(final PlayerDeathEvent e) {
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
	public void onPlayerQuitEvent(final PlayerQuitEvent e) {
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
	public void onPlayerProccessCommandEvent(final PlayerCommandPreprocessEvent e) {
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
	public void onPlayerDropEvent(final PlayerDropItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerPickupItemEvent(final PlayerPickupItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreakEvent(final BlockBreakEvent e) {
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
	public void onBlockPlaceEvent(final BlockPlaceEvent e) {
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
