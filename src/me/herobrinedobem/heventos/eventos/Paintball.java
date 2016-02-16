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
import me.herobrinedobem.heventos.listeners.PaintballListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.EventoBase;

public class Paintball extends EventoBase {

	private final PaintballListener listener;
	private final ArrayList<String> team1 = new ArrayList<String>();
	private final ArrayList<String> team2 = new ArrayList<String>();
	private final ArrayList<String> vencedores = new ArrayList<String>();
	private Location pos1, pos2;

	public Paintball(final YamlConfiguration config) {
		super(config);
		this.listener = new PaintballListener(this);
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(this.listener, HEventos.getHEventos());
		this.pos1 = this.getLocation("Localizacoes.Pos_1");
		this.pos2 = this.getLocation("Localizacoes.Pos_2");
		this.team1.clear();
		this.team2.clear();
	}

	@Override
	public void startEventMethod() {
		final ItemStack arco = new ItemStack(Material.BOW, 1);
		arco.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		boolean team1Ja = false;
		for (final String p : Paintball.this.getParticipantes()) {
			Paintball.this.getPlayerByName(p).getInventory().addItem(arco);
			Paintball.this.getPlayerByName(p).getInventory().addItem(new ItemStack(Material.ARROW, 64));
			if (team1Ja == false) {
				Paintball.this.team1.add(p);
				Paintball.this.darKitPaintball(Paintball.this.getPlayerByName(p), 2);
				Paintball.this.getPlayerByName(p).teleport(Paintball.this.pos1);
				team1Ja = true;
			} else {
				Paintball.this.team2.add(p);
				Paintball.this.darKitPaintball(Paintball.this.getPlayerByName(p), 1);
				Paintball.this.getPlayerByName(p).teleport(Paintball.this.pos2);
				team1Ja = false;
			}
		}
		for (final String s : Paintball.this.getConfig().getStringList("Mensagens.Times")) {
			final StringBuilder time1Builder = new StringBuilder();
			for (final String p1 : Paintball.this.team1) {
				time1Builder.append("§6" + p1 + " ");
			}
			final StringBuilder time2Builder = new StringBuilder();
			for (final String p2 : Paintball.this.team2) {
				time2Builder.append("§6" + p2 + " ");
			}
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$time1$", time1Builder.toString()).replace("$time2$", time2Builder.toString()));
		}
	}

	@Override
	public void scheduledMethod() {
		if ((Paintball.this.isAberto() == false) && (Paintball.this.isOcorrendo() == true)) {
			if (Paintball.this.getParticipantes().size() > 0) {
				if (Paintball.this.team1.size() <= 0) {
					Paintball.this.encerrarEventoComVencedores(Paintball.this.team2);
				}
				if (Paintball.this.team2.size() <= 0) {
					Paintball.this.encerrarEventoComVencedores(Paintball.this.team1);
				}
			} else {
				Paintball.this.encerrarEventoSemVencedores();
			}
			Paintball.this.pos1.getWorld().setTime(17000);
		}
	}

	@Override
	public void cancelEventMethod() {
		this.sendMessageList("Mensagens.Cancelado");
	}

	@Override
	public void resetEvent() {
		super.resetEvent();
		this.pos1 = this.getLocation("Localizacoes.Pos_1");
		this.pos2 = this.getLocation("Localizacoes.Pos_2");
		this.team1.clear();
		this.team2.clear();
		BukkitEventHelper.unregisterEvents(this.listener, HEventos.getHEventos());
	}

	public void encerrarEventoSemVencedores() {
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId());
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId2());
		for (final String s : this.getParticipantes()) {
			this.resetarInventario(this.getPlayerByName(s));
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.getCamarotePlayers()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.sendMessageListVencedor("Mensagens.Sem_Vencedor");
		this.resetEvent();
	}

	public void encerrarEventoComVencedores(final ArrayList<String> team) {
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId());
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.getId2());
		final StringBuilder time1Builder = new StringBuilder();
		for (final String p1 : team) {
			this.resetarInventario(this.getPlayerByName(p1));
			HEventos.getHEventos().getEconomy().depositPlayer(p1, this.getMoney());
			time1Builder.append("§6" + p1 + " ");
		}
		for (final String s : this.getConfig().getStringList("Mensagens.Vencedor")) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$players$", time1Builder.toString()));
		}
		for (final String s : this.getParticipantes()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.getCamarotePlayers()) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.resetEvent();
	}

	private void sendMessageListVencedor(final String list) {
		for (final String s : this.getConfig().getStringList(list)) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", this.vencedores.get(0)));
		}
	}

	private void resetarInventario(final Player p) {
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
	}

	private void darKitPaintball(final Player player, final int time) {
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

	public Location getPos1() {
		return this.pos1;
	}

	public void setPos1(final Location pos1) {
		this.pos1 = pos1;
	}

	public Location getPos2() {
		return this.pos2;
	}

	public void setPos2(final Location pos2) {
		this.pos2 = pos2;
	}

	public ArrayList<String> getTeam1() {
		return this.team1;
	}

	public ArrayList<String> getTeam2() {
		return this.team2;
	}

	@Override
	public ArrayList<String> getVencedores() {
		return this.vencedores;
	}

	public PaintballListener getListener() {
		return this.listener;
	}

}
