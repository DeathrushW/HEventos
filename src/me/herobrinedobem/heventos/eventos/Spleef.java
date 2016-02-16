package me.herobrinedobem.heventos.eventos;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.listeners.SpleefListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.Cuboid;
import me.herobrinedobem.heventos.utils.EventoBase;

public class Spleef extends EventoBase {

	private final SpleefListener listener;
	private boolean podeQuebrar, vencedorEscolhido;
	private boolean regenerarChao;
	private int tempoChaoRegenera, tempoChaoRegeneraCurrent, y, tempoComecar,
			tempoComecarCurrent;

	public Spleef(final YamlConfiguration config) {
		super(config);
		System.out.println("a");
		this.listener = new SpleefListener(this);
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(this.listener, HEventos.getHEventos());
		this.regenerarChao = config.getBoolean("Config.Regenerar_Chao");
		this.tempoChaoRegenera = config.getInt("Config.Tempo_Chao_Regenera");
		this.tempoComecar = config.getInt("Config.Tempo_Comecar");
		this.y = (int) (this.getLocation("Localizacoes.Chao_1").getY() - 2);
		this.podeQuebrar = false;
	}

	@Override
	public void startEventMethod() {
		final Cuboid cubo = new Cuboid(Spleef.this.getLocation("Localizacoes.Chao_1"), Spleef.this.getLocation("Localizacoes.Chao_2"));
		for (final Block b : cubo.getBlocks()) {
			b.setType(Material.getMaterial(Spleef.this.getConfig().getInt("Config.Chao_ID")));
		}
		for (final String p : Spleef.this.getParticipantes()) {
			for (final int i : Spleef.this.getConfig().getIntegerList("Itens_Ao_Iniciar")) {
				Spleef.this.getPlayerByName(p).getInventory().addItem(new ItemStack(Material.getMaterial(i), 1));
			}
		}
	}

	@Override
	public void scheduledMethod() {
		if ((Spleef.this.isOcorrendo() == true) && (Spleef.this.isAberto() == false)) {
			Spleef.this.tempoComecarCurrent--;

			if (Spleef.this.tempoComecarCurrent == 0) {
				Spleef.this.podeQuebrar = true;
			}

			if (Spleef.this.regenerarChao) {
				Spleef.this.tempoChaoRegeneraCurrent--;
				if (Spleef.this.tempoChaoRegeneraCurrent == 0) {
					final Cuboid cubo = new Cuboid(Spleef.this.getLocation("Localizacoes.Chao_1"), Spleef.this.getLocation("Localizacoes.Chao_2"));
					for (final Block b : cubo.getBlocks()) {
						b.setType(Material.getMaterial("Config.Chao_ID"));
					}
					Spleef.this.tempoChaoRegeneraCurrent = Spleef.this.tempoChaoRegenera;
				}
			}

			if (Spleef.this.getParticipantes().size() == 1) {
				Spleef.this.encerrarEventoComVencedor();
			}

			if (Spleef.this.vencedorEscolhido == false) {
				if (Spleef.this.getParticipantes().size() == 0) {
					Spleef.this.encerrarEventoSemVencedor();
				}
			}
		}
	}

	@Override
	public void cancelEventMethod() {
		this.sendMessageList("Mensagens.Cancelado");
	}

	public void encerrarEventoComVencedor() {
		this.vencedorEscolhido = true;
		Player p = null;
		for (final String s : this.getParticipantes()) {
			p = this.getPlayerByName(s);
			p.teleport(this.getSaida());
		}
		for (final String s : this.getCamarotePlayers()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.getConfig().getStringList("Mensagens.Vencedor")) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", p.getName()));
		}
		for (final String sa : this.getConfig().getStringList("Premios.Itens")) {
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", p.getName()));
		}
		HEventos.getHEventos().getEconomy().depositPlayer(p.getName(), this.getMoney());
		if (this.isContarVitoria()) {
			if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
				HEventos.getHEventos().getMysql().addWinnerPoint(p.getName());
			} else {
				HEventos.getHEventos().getSqlite().addWinnerPoint(p.getName());
			}
		}
		this.resetEvent();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId());
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId2());
	}

	public void encerrarEventoSemVencedor() {
		for (final String s : this.getParticipantes()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.getCamarotePlayers()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.sendMessageList("Mensagens.Sem_Vencedor");
		this.resetEvent();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId());
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId2());
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		this.regenerarChao = this.getConfig().getBoolean("Config.Regenerar_Chao");
		this.tempoChaoRegenera = this.getConfig().getInt("Config.Tempo_Chao_Regenera");
		this.tempoComecar = this.getConfig().getInt("Config.Tempo_Comecar");
		this.y = (int) (this.getLocation("Localizacoes.Chao_1").getY() - 2);
		this.podeQuebrar = false;
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	public boolean isPodeQuebrar() {
		return this.podeQuebrar;
	}

	public void setPodeQuebrar(final boolean podeQuebrar) {
		this.podeQuebrar = podeQuebrar;
	}

	public boolean isVencedorEscolhido() {
		return this.vencedorEscolhido;
	}

	public void setVencedorEscolhido(final boolean vencedorEscolhido) {
		this.vencedorEscolhido = vencedorEscolhido;
	}

	public boolean isRegenerarChao() {
		return this.regenerarChao;
	}

	public void setRegenerarChao(final boolean regenerarChao) {
		this.regenerarChao = regenerarChao;
	}

	public int getTempoChaoRegenera() {
		return this.tempoChaoRegenera;
	}

	public void setTempoChaoRegenera(final int tempoChaoRegenera) {
		this.tempoChaoRegenera = tempoChaoRegenera;
	}

	public int getTempoChaoRegeneraCurrent() {
		return this.tempoChaoRegeneraCurrent;
	}

	public void setTempoChaoRegeneraCurrent(final int tempoChaoRegeneraCurrent) {
		this.tempoChaoRegeneraCurrent = tempoChaoRegeneraCurrent;
	}

	public int getY() {
		return this.y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public int getTempoComecar() {
		return this.tempoComecar;
	}

	public void setTempoComecar(final int tempoComecar) {
		this.tempoComecar = tempoComecar;
	}

	public int getTempoComecarCurrent() {
		return this.tempoComecarCurrent;
	}

	public void setTempoComecarCurrent(final int tempoComecarCurrent) {
		this.tempoComecarCurrent = tempoComecarCurrent;
	}

	public SpleefListener getListener() {
		return this.listener;
	}

}
