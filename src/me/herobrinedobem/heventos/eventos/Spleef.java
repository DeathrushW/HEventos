package me.herobrinedobem.heventos.eventos;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;
import me.herobrinedobem.heventos.listeners.SpleefListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.Cuboid;

public class Spleef extends EventoBase {

	private SpleefListener listener;
	private boolean podeQuebrar, vencedorEscolhido;
	private boolean regenerarChao;
	private int tempoChaoRegenera, tempoChaoRegeneraCurrent, y, tempoComecar,
			tempoComecarCurrent;
	
	public Spleef(YamlConfiguration config) {
		super(config);
		listener = new SpleefListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(listener, HEventos.getHEventos());
		regenerarChao = config.getBoolean("Config.Regenerar_Chao");
		tempoChaoRegenera = config.getInt("Config.Tempo_Chao_Regenera");
		tempoComecar = config.getInt("Config.Tempo_Comecar");
		podeQuebrar = false;
		y = (int) (HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_1").getY() - 2);
		Cuboid cubo = new Cuboid(HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_1"), HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_2"));
		for (Block b : cubo.getBlocks()) {
			b.setType(Material.getMaterial(getConfig().getInt("Config.Chao_ID")));
		}
	}

	@Override
	public void startEventMethod() {
		Random r = new Random();
		Cuboid cubo = new Cuboid(HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_1"), HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_2"));
		for (String p : getParticipantes()) {
			for (int i : getConfig().getIntegerList("Itens_Ao_Iniciar")) {
				getPlayerByName(p).getInventory().addItem(new ItemStack(Material.getMaterial(i), 1));
			}
			Location l = cubo.getBlocks().get(r.nextInt(cubo.getBlocks().size() + 1)).getLocation();
			l.setY(l.getY() + 1);
			getPlayerByName(p).teleport(l);
		}
	}

	@Override
	public void scheduledMethod() {
		if ((isOcorrendo() == true) && (isAberto() == false)) {
			if (tempoComecarCurrent == 0) {
				podeQuebrar = true;
			}else{
				tempoComecarCurrent--;
			}
			
			if (regenerarChao) {
				if (tempoChaoRegeneraCurrent == 0) {
					Cuboid cubo = new Cuboid(HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_1"), HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_2"));
					for (Block b : cubo.getBlocks()) {
						b.setType(Material.getMaterial(getConfig().getInt("Config.Chao_ID")));
					}
					tempoChaoRegeneraCurrent = tempoChaoRegenera;
				}else{
					tempoChaoRegeneraCurrent--;
				}
			}
			
			if (getParticipantes().size() <= 0) {
				sendMessageList("Mensagens.Sem_Vencedor");
				stopEvent();
			}else if(getParticipantes().size() == 1){
				Player player = null;
				for(String s : getParticipantes()){
					player = getPlayerByName(s);
				}
				EventoPlayerWinEvent event = new EventoPlayerWinEvent(player, this, 0);
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
		regenerarChao = getConfig().getBoolean("Config.Regenerar_Chao");
		tempoChaoRegenera = getConfig().getInt("Config.Tempo_Chao_Regenera");
		tempoComecar = getConfig().getInt("Config.Tempo_Comecar");
		podeQuebrar = false;
		y = (int) (HEventosAPI.getLocation(getConfig(), "Localizacoes.Chao_1").getY() - 2);
		BukkitEventHelper.unregisterEvents(listener, HEventos.getHEventos());
	}

	public boolean isPodeQuebrar() {
		return this.podeQuebrar;
	}

	public void setPodeQuebrar(boolean podeQuebrar) {
		this.podeQuebrar = podeQuebrar;
	}

	public boolean isVencedorEscolhido() {
		return this.vencedorEscolhido;
	}

	public void setVencedorEscolhido(boolean vencedorEscolhido) {
		this.vencedorEscolhido = vencedorEscolhido;
	}

	public boolean isRegenerarChao() {
		return this.regenerarChao;
	}

	public void setRegenerarChao(boolean regenerarChao) {
		this.regenerarChao = regenerarChao;
	}

	public int getTempoChaoRegenera() {
		return this.tempoChaoRegenera;
	}

	public void setTempoChaoRegenera(int tempoChaoRegenera) {
		this.tempoChaoRegenera = tempoChaoRegenera;
	}

	public int getTempoChaoRegeneraCurrent() {
		return this.tempoChaoRegeneraCurrent;
	}

	public void setTempoChaoRegeneraCurrent(int tempoChaoRegeneraCurrent) {
		this.tempoChaoRegeneraCurrent = tempoChaoRegeneraCurrent;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getTempoComecar() {
		return this.tempoComecar;
	}

	public void setTempoComecar(int tempoComecar) {
		this.tempoComecar = tempoComecar;
	}

	public int getTempoComecarCurrent() {
		return this.tempoComecarCurrent;
	}

	public void setTempoComecarCurrent(int tempoComecarCurrent) {
		this.tempoComecarCurrent = tempoComecarCurrent;
	}

	public SpleefListener getListener() {
		return this.listener;
	}

}
