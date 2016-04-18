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
import me.herobrinedobem.heventos.listeners.BatataQuenteListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.EventoBase;

public class BatataQuente extends EventoBase {

	private final BatataQuenteListener listener;
	private int tempoBatataCurrent, tempoBatata;
	private Player playerComBatata, vencedor;

	public BatataQuente(final YamlConfiguration config) {
		super(config);
		this.listener = new BatataQuenteListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(this.listener, HEventos.getHEventos());
		this.tempoBatata = config.getInt("Config.Tempo_Batata_Explodir");
		this.vencedor = null;
		this.playerComBatata = null;
		this.tempoBatataCurrent = this.tempoBatata;
	}

	@Override
	public void startEventMethod() {
		final Random r = new Random();
		BatataQuente.this.playerComBatata = BatataQuente.this.getPlayerByName(BatataQuente.this.getParticipantes().get(r.nextInt(BatataQuente.this.getParticipantes().size())));
		BatataQuente.this.playerComBatata.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, 1));
		for (final String s : BatataQuente.this.getConfig().getStringList("Mensagens.Esta_Com_Batata")) {
			for (final String sa : BatataQuente.this.getParticipantes()) {
				final Player sab = BatataQuente.this.getPlayerByName(sa);
				sab.sendMessage(s.replace("&", "§").replace("$player$", BatataQuente.this.playerComBatata.getName()));
			}
		}
	}

	@Override
	public void scheduledMethod() {
		if ((BatataQuente.this.isOcorrendo() == true) && (BatataQuente.this.isAberto() == false) && (BatataQuente.this.playerComBatata != null)) {
			if (BatataQuente.this.getParticipantes().size() <= 1) {
				Player p = null;
				for (final String s : BatataQuente.this.getParticipantes()) {
					p = BatataQuente.this.getPlayerByName(s);
				}
				for (final String sa : BatataQuente.this.getConfig().getStringList("Premios.Itens")) {
					HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", p.getName()));
				}
				HEventos.getHEventos().getEconomy().depositPlayer(p.getName(), BatataQuente.this.getMoney());
				if (BatataQuente.this.isContarVitoria()) {
					if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
						HEventos.getHEventos().getMysql().addWinnerPoint(p.getName());
					} else {
						HEventos.getHEventos().getSqlite().addWinnerPoint(p.getName());
					}
				}
				BatataQuente.this.stopEvent();
			}

			if (BatataQuente.this.tempoBatataCurrent > 0) {
				BatataQuente.this.tempoBatataCurrent--;
				final Location loc = BatataQuente.this.playerComBatata.getLocation();
				final Firework firework = BatataQuente.this.playerComBatata.getWorld().spawn(loc, Firework.class);
				final FireworkMeta data = firework.getFireworkMeta();
				data.addEffects(FireworkEffect.builder().withColor(Color.RED).with(Type.BALL).build());
				data.setPower(2);
				firework.setFireworkMeta(data);
				if ((BatataQuente.this.tempoBatataCurrent == 29) || (BatataQuente.this.tempoBatataCurrent == 20) || (BatataQuente.this.tempoBatataCurrent == 10) || (BatataQuente.this.tempoBatataCurrent == 5) || (BatataQuente.this.tempoBatataCurrent == 4) || (BatataQuente.this.tempoBatataCurrent == 3) || (BatataQuente.this.tempoBatataCurrent == 2) || (BatataQuente.this.tempoBatataCurrent == 1)) {
					for (final String s : BatataQuente.this.getParticipantes()) {
						final Player p = BatataQuente.this.getPlayerByName(s);
						p.playSound(p.getLocation(), Sound.CLICK, 1.0f, 1.0f);
					}
					for (final String s : BatataQuente.this.getConfig().getStringList("Mensagens.Tempo")) {
						for (final String sa : BatataQuente.this.getParticipantes()) {
							final Player p = BatataQuente.this.getPlayerByName(sa);
							p.sendMessage(s.replace("&", "§").replace("$tempo$", BatataQuente.this.tempoBatataCurrent + ""));
						}
					}
				}
			} else {
				BatataQuente.this.playerComBatata.getInventory().removeItem(new ItemStack(Material.POTATO_ITEM, 1));
				BatataQuente.this.playerComBatata.teleport(BatataQuente.this.getSaida());
				BatataQuente.this.getParticipantes().remove(BatataQuente.this.playerComBatata.getName());
				for (final String s : BatataQuente.this.getConfig().getStringList("Mensagens.Eliminado")) {
					for (final String sa : BatataQuente.this.getParticipantes()) {
						final Player p = BatataQuente.this.getPlayerByName(sa);
						p.sendMessage(s.replace("&", "§").replace("$player$", BatataQuente.this.playerComBatata.getName()));
					}
				}
				final Random r = new Random();
				BatataQuente.this.playerComBatata = BatataQuente.this.getPlayerByName(BatataQuente.this.getParticipantes().get(r.nextInt(BatataQuente.this.getParticipantes().size())));
				for (final String s : BatataQuente.this.getConfig().getStringList("Mensagens.Esta_Com_Batata")) {
					BatataQuente.this.playerComBatata.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, 1));
					for (final String sa : BatataQuente.this.getParticipantes()) {
						final Player p = BatataQuente.this.getPlayerByName(sa);
						p.sendMessage(s.replace("&", "§").replace("$player$", BatataQuente.this.playerComBatata.getName()));
					}
				}
				BatataQuente.this.tempoBatataCurrent = BatataQuente.this.tempoBatata;
			}
		}
	}

	@Override
	public void cancelEventMethod() {
		this.sendMessageList("Mensagens.Cancelado");
	}

	@Override
	public void stopEventMethod() {
		this.sendMessageListVencedor("Mensagens.Vencedor");
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		this.tempoBatata = this.getConfig().getInt("Config.Tempo_Batata_Explodir");
		this.vencedor = null;
		this.playerComBatata = null;
		this.tempoBatataCurrent = this.tempoBatata;
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	private void sendMessageListVencedor(final String list) {
		Player p = null;
		for (final String s : BatataQuente.this.getParticipantes()) {
			p = BatataQuente.this.getPlayerByName(s);
		}
		for (final String s : this.getConfig().getStringList(list)) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", p.getName()));
		}
	}

	public int getTempoBatataCurrent() {
		return this.tempoBatataCurrent;
	}

	public void setTempoBatataCurrent(final int tempoBatataCurrent) {
		this.tempoBatataCurrent = tempoBatataCurrent;
	}

	public int getTempoBatata() {
		return this.tempoBatata;
	}

	public void setTempoBatata(final int tempoBatata) {
		this.tempoBatata = tempoBatata;
	}

	public Player getPlayerComBatata() {
		return this.playerComBatata;
	}

	public void setPlayerComBatata(final Player playerComBatata) {
		this.playerComBatata = playerComBatata;
	}

	public Player getVencedor() {
		return this.vencedor;
	}

	public void setVencedor(final Player vencedor) {
		this.vencedor = vencedor;
	}

	public BatataQuenteListener getListener() {
		return this.listener;
	}

}
