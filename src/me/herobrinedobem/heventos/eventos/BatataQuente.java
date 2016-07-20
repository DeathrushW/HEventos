package me.herobrinedobem.heventos.eventos;

import java.util.Random;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerLoseEvent;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;
import me.herobrinedobem.heventos.listeners.BatataQuenteListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class BatataQuente extends EventoBase {

	private BatataQuenteListener listener;
	private int tempoBatataCurrent, tempoBatata;
	private Player playerComBatata, vencedor;

	public BatataQuente(YamlConfiguration config) {
		super(config);
		listener = new BatataQuenteListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(listener, HEventos.getHEventos());
		tempoBatata = config.getInt("Config.Tempo_Batata_Explodir");
		vencedor = null;
		playerComBatata = null;
		tempoBatataCurrent = tempoBatata;
	}

	@Override
	public void startEventMethod() {
		for(String s : getParticipantes()){
			getPlayerByName(s).teleport(HEventosAPI.getLocation(getConfig(), "Localizacoes.Entrada"));
		}
		Random r = new Random();
		playerComBatata = getPlayerByName(getParticipantes().get(r.nextInt(getParticipantes().size())));
		playerComBatata.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, 1));
		for (String s : getConfig().getStringList("Mensagens.Esta_Com_Batata")) {
			for (String sa : getParticipantes()) {
				getPlayerByName(sa).sendMessage(s.replace("&", "§").replace("$player$", playerComBatata.getName()));
			}
		}
	}

	@Override
	public void scheduledMethod() {
		if ((isOcorrendo() == true) && (isAberto() == false) && (playerComBatata != null)) {
			if (getParticipantes().size() == 1) {
				Player player = null;
				for(String s : getParticipantes()){
					player = getPlayerByName(s);
				}
				EventoPlayerWinEvent event = new EventoPlayerWinEvent(player, this, 0);
				HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
				stopEvent();
			}else if(getParticipantes().size() == 0){
				sendMessageList("Mensagens.Sem_Vencedor");
				stopEvent();
			}else{
				if (tempoBatataCurrent > 0) {
					tempoBatataCurrent--;
					Location loc = playerComBatata.getLocation();
					Firework firework = playerComBatata.getWorld().spawn(loc, Firework.class);
					FireworkMeta data = firework.getFireworkMeta();
					data.addEffects(FireworkEffect.builder().withColor(Color.RED).with(Type.BALL).build());
					data.setPower(2);
					firework.setFireworkMeta(data);
					if ((tempoBatataCurrent == 29) || (tempoBatataCurrent == 20) || (tempoBatataCurrent == 10) || (tempoBatataCurrent == 5) || (tempoBatataCurrent == 4) || (tempoBatataCurrent == 3) || (tempoBatataCurrent == 2) || (tempoBatataCurrent == 1)) {
						for (String s : getParticipantes()) {
							Player p = getPlayerByName(s);
							p.playSound(p.getLocation(), Sound.CLICK, 1.0f, 1.0f);
						}
						for (String s : getConfig().getStringList("Mensagens.Tempo")) {
							for (String sa : getParticipantes()) {
								Player p = getPlayerByName(sa);
								p.sendMessage(s.replace("&", "§").replace("$tempo$", tempoBatataCurrent + ""));
							}
						}
					}
				} else {
					playerComBatata.getInventory().removeItem(new ItemStack(Material.POTATO_ITEM, 1));
					EventoPlayerLoseEvent event = new EventoPlayerLoseEvent(playerComBatata, HEventos.getHEventos().getEventosController().getEvento());
					HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
					for (String s : getConfig().getStringList("Mensagens.Eliminado")) {
						for (String sa : getParticipantes()) {
							Player p = getPlayerByName(sa);
							p.sendMessage(s.replace("&", "§").replace("$player$", playerComBatata.getName()));
						}
					}
					Random r = new Random();
					playerComBatata = getPlayerByName(getParticipantes().get(r.nextInt(getParticipantes().size())));
					for (String s : getConfig().getStringList("Mensagens.Esta_Com_Batata")) {
						playerComBatata.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, 1));
						for (String sa : getParticipantes()) {
							Player p = getPlayerByName(sa);
							p.sendMessage(s.replace("&", "§").replace("$player$", playerComBatata.getName()));
						}
					}
					tempoBatataCurrent = tempoBatata;
				}
			}
		}
	}

	@Override
	public void cancelEventMethod() {
		sendMessageList("Mensagens.Cancelado");
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		tempoBatata = getConfig().getInt("Config.Tempo_Batata_Explodir");
		vencedor = null;
		playerComBatata = null;
		tempoBatataCurrent = tempoBatata;
		BukkitEventHelper.unregisterEvents(listener, HEventos.getHEventos());
	}

	public int getTempoBatataCurrent() {
		return this.tempoBatataCurrent;
	}

	public void setTempoBatataCurrent(int tempoBatataCurrent) {
		this.tempoBatataCurrent = tempoBatataCurrent;
	}

	public int getTempoBatata() {
		return this.tempoBatata;
	}

	public void setTempoBatata(int tempoBatata) {
		this.tempoBatata = tempoBatata;
	}

	public Player getPlayerComBatata() {
		return this.playerComBatata;
	}

	public void setPlayerComBatata(Player playerComBatata) {
		this.playerComBatata = playerComBatata;
	}

	public Player getVencedor() {
		return this.vencedor;
	}

	public void setVencedor(Player vencedor) {
		this.vencedor = vencedor;
	}

	public BatataQuenteListener getListener() {
		return this.listener;
	}

}
