package me.herobrinedobem.heventos.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.eventos.Paintball;
import me.herobrinedobem.heventos.utils.EventoBaseListener;

public class PaintballListener extends EventoBaseListener {

	@Override
	@EventHandler
	public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent e) {
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
						final Paintball paintball = (Paintball) HEventos.getHEventos().getEventosController().getEvento();
						if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(atingido.getName()) && HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(atirou.getName())) {
							if (paintball.getTime1().contains(atirou.getName())) {
								if (paintball.getTime1().contains(atingido.getName())) {
									matou = false;
								} else {
									matou = true;
								}
							} else if (paintball.getTime2().contains(atirou.getName())) {
								if (paintball.getTime2().contains(atingido.getName())) {
									matou = false;
								} else {
									matou = true;
								}
							}
							if (matou) {
								if (paintball.getTime2().contains(atingido.getName())) {
									paintball.getTime2().remove(atingido.getName());
								} else {
									paintball.getTime1().remove(atingido.getName());
								}
								atingido.getInventory().setHelmet(null);
								atingido.getInventory().setChestplate(null);
								atingido.getInventory().setLeggings(null);
								atingido.getInventory().setBoots(null);
								atingido.getInventory().clear();
								HEventos.getHEventos().getEconomy().depositPlayer(atirou.getName(), paintball.getConfig().getDouble("Premios.Money_Kill"));
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

	@Override
	@EventHandler
	public void onPlayerQuitEvent(final PlayerQuitEvent e) {
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

}
