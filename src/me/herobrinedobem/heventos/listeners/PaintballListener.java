package me.herobrinedobem.heventos.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBaseListener;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerLoseEvent;
import me.herobrinedobem.heventos.eventos.Paintball;

public class PaintballListener extends EventoBaseListener {

	@Override
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if ((HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) && (HEventos.getHEventos().getEventosController().getEvento().isOcorrendo() == true)) {
				if (e.getDamager() instanceof Player) {
					Player p = (Player) e.getDamager();
					if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
						if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
					if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
				} else if (e.getDamager() instanceof Arrow) {
					Arrow projectile = (Arrow) e.getDamager();
					if (projectile.getShooter() instanceof Player) {
						Player atirou = (Player) projectile.getShooter();
						Player atingido = (Player) e.getEntity();
						atingido.setHealth(20);
						Paintball paintball = (Paintball) HEventos.getHEventos().getEventosController().getEvento();
						if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(atingido.getName()) && HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(atirou.getName())) {
							EventoPlayerLoseEvent event = new EventoPlayerLoseEvent(atingido, HEventos.getHEventos().getEventosController().getEvento());
							HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
							HEventos.getHEventos().getEconomy().depositPlayer(atirou.getName(), paintball.getConfig().getDouble("Premios.Money_Kill"));
							atingido.teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
							HEventos.getHEventos().getEventosController().getEvento().getParticipantes().remove(atingido.getName());
							atingido.sendMessage(HEventos.getHEventos().getEventosController().getEvento().getConfig().getString("Mensagens.Eliminado").replace("&", "§").replace("$player$", atirou.getName()));
							atirou.sendMessage(HEventos.getHEventos().getEventosController().getEvento().getConfig().getString("Mensagens.Eliminou").replace("&", "§").replace("$player$", atingido.getName()));
						}
					}
				}
			}
		}
	}

	@Override
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(e.getPlayer().getName())) {
				EventoPlayerLoseEvent event = new EventoPlayerLoseEvent(e.getPlayer(), HEventos.getHEventos().getEventosController().getEvento());
				HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
				for (String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
					Player p = HEventos.getHEventos().getServer().getPlayer(s);
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
