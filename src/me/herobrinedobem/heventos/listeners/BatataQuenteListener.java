package me.herobrinedobem.heventos.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBaseListener;
import me.herobrinedobem.heventos.eventos.BatataQuente;

public class BatataQuenteListener extends EventoBaseListener {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getEvento() != null) {
			if (e.getRightClicked() instanceof Player) {
				Player pa = (Player) e.getRightClicked();
				if (HEventos.getHEventos().getEventosController().getEvento().isAberto() == false) {
					BatataQuente batataQuente = (BatataQuente) HEventos.getHEventos().getEventosController().getEvento();
					if (e.getPlayer().getName().equalsIgnoreCase(batataQuente.getPlayerComBatata().getName())) {
						if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(pa.getName())) {
							batataQuente.setPlayerComBatata((Player) e.getRightClicked());
							for (String s : HEventos.getHEventos().getEventosController().getEvento().getConfig().getStringList("Mensagens.Esta_Com_Batata")) {
								for (String sa : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
									Player p = HEventos.getHEventos().getServer().getPlayer(sa);
									p.sendMessage(s.replace("&", "§").replace("$player$", batataQuente.getPlayerComBatata().getName()));
								}
							}
						}
					}
				}
			}
		}
	}

}
