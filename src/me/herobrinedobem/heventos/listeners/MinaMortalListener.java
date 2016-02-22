package me.herobrinedobem.heventos.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import me.herobrinedobem.heventos.HEventos;

public class MinaMortalListener implements Listener {

	@EventHandler
	public void onPotionSplashEvent(final PotionSplashEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (e.getPotion().getShooter() instanceof Player) {
				final Player p = (Player) e.getPotion().getShooter();
				if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

}
