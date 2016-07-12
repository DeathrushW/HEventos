package me.herobrinedobem.heventos.eventos;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.listeners.BowSpleefListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.Cuboid;

public class BowSpleef extends EventoBase {

	private final BowSpleefListener listener;
	private Cuboid chao;
	private boolean podeQuebrar;
	private int tempoInicial, tempoRegenera;
	private Location chao1, chao2;

	public BowSpleef(final YamlConfiguration config) {
		super(config);
		this.listener = new BowSpleefListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(this.listener, HEventos.getHEventos());
		this.podeQuebrar = false;
		this.tempoInicial = this.getConfig().getInt("Config.Tempo_Comecar");
		this.tempoRegenera = this.getConfig().getInt("Config.Tempo_Chao_Regenera");
		this.chao1 = this.getLocation("Localizacoes.Chao_1");
		this.chao2 = this.getLocation("Localizacoes.Chao_2");
		this.chao = new Cuboid(this.chao1, this.chao2);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void startEventMethod() {
		final ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
		for (final String s : this.getParticipantes()) {
			this.getPlayerByName(s).getInventory().addItem(bow);
			this.getPlayerByName(s).getInventory().addItem(new ItemStack(Material.ARROW));
			this.getPlayerByName(s).updateInventory();
		}
		for (final Block b : this.chao.getBlocks()) {
			b.setType(Material.getMaterial(this.getConfig().getInt("Config.Chao_ID")));
		}
	}

	@Override
	public void scheduledMethod() {
		if ((this.isOcorrendo() == true) && (this.isAberto() == false)) {
			if (this.tempoInicial > 0) {
				this.tempoInicial--;
				this.podeQuebrar = false;
			} else {
				this.podeQuebrar = true;
			}
			if (this.tempoRegenera > 0) {
				this.tempoRegenera--;
			} else {
				for (final Block b : this.chao.getBlocks()) {
					b.setType(Material.getMaterial(this.getConfig().getInt("Config.Chao_ID")));
				}
				this.tempoRegenera = this.getConfig().getInt("Config.Tempo_Chao_Regenera");
			}
			if (this.getParticipantes().size() <= 0) {
				this.sendMessageList("Mensagens.Sem_Vencedor");
				this.stopEvent();
			} else if (this.getParticipantes().size() == 1) {
				for (final String sa : this.getConfig().getStringList("Mensagens.Vencedor")) {
					HEventos.getHEventos().getServer().broadcastMessage(sa.replace("$player$", this.getPlayerByName(this.getParticipantes().get(0)).getName()).replace("&", "§"));
				}
				this.stopEvent();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void cancelEventMethod() {
		if (this.isAberto() == false) {
			for (final String s : this.getParticipantes()) {
				this.getPlayerByName(s).getInventory().clear();
				this.getPlayerByName(s).updateInventory();
			}
		}
		this.resetEvent();
		this.sendMessageList("Mensagens.Cancelado");
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		this.podeQuebrar = false;
		this.tempoInicial = this.getConfig().getInt("Config.Tempo_Comecar");
		this.tempoRegenera = this.getConfig().getInt("Config.Tempo_Chao_Regenera");
		this.chao1 = this.getLocation("Localizacoes.Chao_1");
		this.chao2 = this.getLocation("Localizacoes.Chao_2");
		this.chao = new Cuboid(this.chao1, this.chao2);
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	public boolean isPodeQuebrar() {
		return this.podeQuebrar;
	}

	public void setPodeQuebrar(final boolean podeQuebrar) {
		this.podeQuebrar = podeQuebrar;
	}

	public int getTempoInicial() {
		return this.tempoInicial;
	}

	public void setTempoInicial(final int tempoInicial) {
		this.tempoInicial = tempoInicial;
	}

	public int getTempoRegenera() {
		return this.tempoRegenera;
	}

	public void setTempoRegenera(final int tempoRegenera) {
		this.tempoRegenera = tempoRegenera;
	}

	public Cuboid getChao() {
		return this.chao;
	}

	public Location getChao1() {
		return this.chao1;
	}

	public Location getChao2() {
		return this.chao2;
	}

}
