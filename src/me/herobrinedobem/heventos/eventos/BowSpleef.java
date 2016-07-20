package me.herobrinedobem.heventos.eventos;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;
import me.herobrinedobem.heventos.listeners.BowSpleefListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.Cuboid;

public class BowSpleef extends EventoBase {

	private BowSpleefListener listener;
	private Cuboid chao;
	private boolean podeQuebrar;
	private int tempoInicial, tempoRegenera;

	public BowSpleef(YamlConfiguration config) {
		super(config);
		listener = new BowSpleefListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(listener, HEventos.getHEventos());
		podeQuebrar = false;
		tempoInicial = getConfig().getInt("Config.Tempo_Comecar");
		tempoRegenera = getConfig().getInt("Config.Tempo_Chao_Regenera");
		chao = new Cuboid(HEventosAPI.getLocation(config, "Localizacoes.Chao_1"), HEventosAPI.getLocation(config, "Localizacoes.Chao_2"));
		for (Block b : chao.getBlocks()) {
			b.setType(Material.getMaterial(getConfig().getInt("Config.Chao_ID")));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void startEventMethod() {
		Random r = new Random();
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
		for (String s : getParticipantes()) {
			getPlayerByName(s).getInventory().addItem(bow);
			getPlayerByName(s).getInventory().addItem(new ItemStack(Material.ARROW));
			getPlayerByName(s).updateInventory();
			Location l = chao.getBlocks().get(r.nextInt(chao.getBlocks().size() + 1)).getLocation();
			l.setY(l.getY() + 1);
			getPlayerByName(s).teleport(l);
		}
	}

	@Override
	public void scheduledMethod() {
		if ((isOcorrendo() == true) && (isAberto() == false)) {
			if (tempoInicial > 0) {
				tempoInicial--;
				podeQuebrar = false;
			} else {
				podeQuebrar = true;
			}
			if (tempoRegenera > 0) {
				tempoRegenera--;
			} else {
				for (Block b : chao.getBlocks()) {
					b.setType(Material.getMaterial(getConfig().getInt("Config.Chao_ID")));
				}
				tempoRegenera = getConfig().getInt("Config.Tempo_Chao_Regenera");
			}
			if (getParticipantes().size() <= 0) {
				sendMessageList("Mensagens.Sem_Vencedor");
				stopEvent();
			} else if (getParticipantes().size() == 1) {
				EventoPlayerWinEvent event = new EventoPlayerWinEvent(getPlayerByName(getParticipantes().get(0)), this, 0);
				HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
				stopEvent();
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
		podeQuebrar = false;
		tempoInicial = getConfig().getInt("Config.Tempo_Comecar");
		tempoRegenera = getConfig().getInt("Config.Tempo_Chao_Regenera");
		chao = new Cuboid(HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_1"), HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_2"));
		BukkitEventHelper.unregisterEvents(listener, HEventos.getHEventos());
	}

	public boolean isPodeQuebrar() {
		return this.podeQuebrar;
	}

	public void setPodeQuebrar(boolean podeQuebrar) {
		this.podeQuebrar = podeQuebrar;
	}

	public int getTempoInicial() {
		return this.tempoInicial;
	}

	public void setTempoInicial(int tempoInicial) {
		this.tempoInicial = tempoInicial;
	}

	public int getTempoRegenera() {
		return this.tempoRegenera;
	}

	public void setTempoRegenera(int tempoRegenera) {
		this.tempoRegenera = tempoRegenera;
	}

	public Cuboid getChao() {
		return this.chao;
	}

}
