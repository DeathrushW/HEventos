package me.herobrinedobem.heventos.eventos;

import java.util.ArrayList;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.listeners.PaintballListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class Paintball extends EventoBase {

	private final PaintballListener listener;
	private final ArrayList<String> time1 = new ArrayList<>();
	private final ArrayList<String> time2 = new ArrayList<>();
	private Location locTime1, locTime2;

	public Paintball(final YamlConfiguration config) {
		super(config);
		this.listener = new PaintballListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(this.listener, HEventos.getHEventos());
		this.locTime1 = this.getLocation("Localizacoes.Pos_1");
		this.locTime2 = this.getLocation("Localizacoes.Pos_2");
	}

	@Override
	public void startEvent() {
		if (this.getChamadascurrent() >= 1) {
			this.setChamadascurrent(this.getChamadascurrent() - 1);
			this.setOcorrendo(true);
			this.setAberto(true);
			if (this.isVip()) {
				this.sendMessageList("Mensagens.Aberto_VIP");
			} else {
				this.sendMessageList("Mensagens.Aberto");
			}
		} else if (this.getChamadascurrent() == 0) {
			if (this.getParticipantes().size() >= 1) {
				this.setAberto(false);
				this.setParte1(true);
				for (final String sa : this.getCamarotePlayers()) {
					this.getPlayerByName(sa).teleport(this.getCamarote());
				}
				for (final String p : this.getParticipantes()) {
					if (this.isContarParticipacao()) {
						if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
							HEventos.getHEventos().getMysql().addPartipationPoint(p);
						} else {
							HEventos.getHEventos().getSqlite().addPartipationPoint(p);
						}
					}
				}
				this.sendMessageList("Mensagens.Iniciando");
				this.separarTimes();
				this.darKit();
			} else {
				this.resetEvent();
				this.sendMessageList("Mensagens.Cancelado");
				HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId());
			}
		}
	}

	@Override
	public void scheduledMethod() {
		if ((this.isOcorrendo() == true) && (this.isAberto() == false)) {
			if (this.getParticipantes().size() <= 0) {
				this.sendMessageList("Mensagens.Sem_Vencedor");
				this.stopEvent();
			}
			if ((this.time1.size() > 0) && (this.time2.size() <= 0)) {
				final StringBuilder venc = new StringBuilder();
				for (final String s : this.time1) {
					venc.append(s + " ");
				}
				for (final String s : this.getConfig().getStringList("Mensagens.Vencedor")) {
					HEventos.getHEventos().getServer().broadcastMessage(s.replaceAll("&", "§").replace("$players$", venc.toString()));
				}
				this.stopEvent();
			} else if ((this.time2.size() > 0) && (this.time1.size() <= 0)) {
				final StringBuilder venc = new StringBuilder();
				for (final String s : this.time2) {
					venc.append(s + " ");
				}
				for (final String s : this.getConfig().getStringList("Mensagens.Vencedor")) {
					HEventos.getHEventos().getServer().broadcastMessage(s.replaceAll("&", "§").replace("$players$", venc.toString()));
				}
				this.stopEvent();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void cancelEventMethod() {
		for (final String s : this.getParticipantes()) {
			this.getPlayerByName(s).getInventory().setHelmet(null);
			this.getPlayerByName(s).getInventory().setChestplate(null);
			this.getPlayerByName(s).getInventory().setLeggings(null);
			this.getPlayerByName(s).getInventory().setBoots(null);
			this.getPlayerByName(s).getInventory().clear();
			this.getPlayerByName(s).updateInventory();
		}
		this.sendMessageList("Mensagens.Cancelado");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void stopEventMethod() {
		for (final String s : this.getParticipantes()) {
			this.getPlayerByName(s).getInventory().setHelmet(null);
			this.getPlayerByName(s).getInventory().setChestplate(null);
			this.getPlayerByName(s).getInventory().setLeggings(null);
			this.getPlayerByName(s).getInventory().setBoots(null);
			this.getPlayerByName(s).getInventory().clear();
			this.getPlayerByName(s).updateInventory();
		}
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		this.locTime1 = this.getLocation("Localizacoes.Pos_1");
		this.locTime2 = this.getLocation("Localizacoes.Pos_2");
		this.time1.clear();
		this.time2.clear();
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	private void separarTimes() {
		boolean selecionado = false;
		for (final String s : this.getParticipantes()) {
			if (selecionado == false) {
				this.time1.add(s);
				this.darUniforme(this.getPlayerByName(s), 1);
				this.getPlayerByName(s).teleport(this.locTime1);
				selecionado = true;
			} else {
				this.time2.add(s);
				this.darUniforme(this.getPlayerByName(s), 2);
				this.getPlayerByName(s).teleport(this.locTime2);
				selecionado = false;
			}
		}

		final StringBuilder time1MSG = new StringBuilder();
		final StringBuilder time2MSG = new StringBuilder();

		for (final String s : this.time1) {
			time1MSG.append(s + " ");
		}

		for (final String s : this.time2) {
			time2MSG.append(s + " ");
		}

		for (final String s : this.getConfig().getStringList("Mensagens.Times")) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$time1$", time1MSG.toString()).replace("$time2$", time2MSG.toString()));

		}
	}

	@SuppressWarnings("deprecation")
	private void darKit() {
		final ItemStack arco = new ItemStack(Material.BOW, 1);
		arco.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		for (final String s : this.getParticipantes()) {
			this.getPlayerByName(s).getInventory().addItem(arco);
			this.getPlayerByName(s).getInventory().addItem(new ItemStack(Material.ARROW, 64));
			this.getPlayerByName(s).updateInventory();
		}
	}

	private void darUniforme(final Player player, final int time) {
		if (time == 1) {
			final ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
			final LeatherArmorMeta lam = (LeatherArmorMeta) lhelmet.getItemMeta();
			lam.setColor(Color.RED);
			lhelmet.setItemMeta(lam);

			final ItemStack lChest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			final LeatherArmorMeta lcm = (LeatherArmorMeta) lChest.getItemMeta();
			lcm.setColor(Color.RED);
			lChest.setItemMeta(lcm);

			final ItemStack lLegg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			final LeatherArmorMeta llg = (LeatherArmorMeta) lLegg.getItemMeta();
			llg.setColor(Color.RED);
			lLegg.setItemMeta(llg);

			final ItemStack lBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
			final LeatherArmorMeta lbo = (LeatherArmorMeta) lBoots.getItemMeta();
			lbo.setColor(Color.RED);
			lBoots.setItemMeta(lbo);

			player.getInventory().setHelmet(lhelmet);
			player.getInventory().setChestplate(lChest);
			player.getInventory().setLeggings(lLegg);
			player.getInventory().setBoots(lBoots);

		} else if (time == 2) {
			final ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
			final LeatherArmorMeta lam = (LeatherArmorMeta) lhelmet.getItemMeta();
			lam.setColor(Color.BLUE);
			lhelmet.setItemMeta(lam);

			final ItemStack lChest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			final LeatherArmorMeta lcm = (LeatherArmorMeta) lChest.getItemMeta();
			lcm.setColor(Color.BLUE);
			lChest.setItemMeta(lcm);

			final ItemStack lLegg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			final LeatherArmorMeta llg = (LeatherArmorMeta) lLegg.getItemMeta();
			llg.setColor(Color.BLUE);
			lLegg.setItemMeta(llg);

			final ItemStack lBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
			final LeatherArmorMeta lbo = (LeatherArmorMeta) lBoots.getItemMeta();
			lbo.setColor(Color.BLUE);
			lBoots.setItemMeta(lbo);

			player.getInventory().setHelmet(lhelmet);
			player.getInventory().setChestplate(lChest);
			player.getInventory().setLeggings(lLegg);
			player.getInventory().setBoots(lBoots);
		}
	}

	public Location getLocTime1() {
		return this.locTime1;
	}

	public void setLocTime1(final Location locTime1) {
		this.locTime1 = locTime1;
	}

	public Location getLocTime2() {
		return this.locTime2;
	}

	public void setLocTime2(final Location locTime2) {
		this.locTime2 = locTime2;
	}

	public ArrayList<String> getTime1() {
		return this.time1;
	}

	public ArrayList<String> getTime2() {
		return this.time2;
	}

}
