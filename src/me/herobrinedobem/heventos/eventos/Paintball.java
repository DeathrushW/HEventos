package me.herobrinedobem.heventos.eventos;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;
import me.herobrinedobem.heventos.listeners.PaintballListener;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.Cuboid;

public class Paintball extends EventoBase {

	private PaintballListener listener;

	public Paintball(YamlConfiguration config) {
		super(config);
		listener = new PaintballListener();
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(listener, HEventos.getHEventos());
	}

	@Override
	public void startEventMethod() {
		Cuboid cubo = new Cuboid(HEventosAPI.getLocation(getConfig(), "Localizacoes.Pos_1"), HEventosAPI.getLocation(getConfig(), "Localizacoes.Pos_2"));
		Random r = new Random();
		for(String s : getParticipantes()){
			darKit(getPlayerByName(s));
			Location l = cubo.getBlocks().get(r.nextInt(cubo.getBlocks().size() + 1)).getLocation();
			l.setY(l.getY() + 1);
			getPlayerByName(s).teleport(l);
		}
	}
	
	@Override
	public void scheduledMethod() {
		if ((isOcorrendo() == true) && (isAberto() == false)) {
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
		BukkitEventHelper.unregisterEvents(listener, HEventos.getHEventos());
	}

	@SuppressWarnings("deprecation")
	private void darKit(Player player) {
		ItemStack arco = new ItemStack(Material.BOW, 1);
		arco.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		for (String s : getParticipantes()) {
			getPlayerByName(s).getInventory().addItem(arco);
			getPlayerByName(s).getInventory().addItem(new ItemStack(Material.ARROW, 64));
			getPlayerByName(s).updateInventory();
		}
		ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta lam = (LeatherArmorMeta) lhelmet.getItemMeta();
		lam.setColor(HEventosAPI.getRandomColor());
		lhelmet.setItemMeta(lam);

		ItemStack lChest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lcm = (LeatherArmorMeta) lChest.getItemMeta();
		lcm.setColor(HEventosAPI.getRandomColor());
		lChest.setItemMeta(lcm);

		ItemStack lLegg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta llg = (LeatherArmorMeta) lLegg.getItemMeta();
		llg.setColor(HEventosAPI.getRandomColor());
		lLegg.setItemMeta(llg);

		ItemStack lBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
		LeatherArmorMeta lbo = (LeatherArmorMeta) lBoots.getItemMeta();
		lbo.setColor(HEventosAPI.getRandomColor());
		lBoots.setItemMeta(lbo);

		player.getInventory().setHelmet(lhelmet);
		player.getInventory().setChestplate(lChest);
		player.getInventory().setLeggings(lLegg);
		player.getInventory().setBoots(lBoots);
		player.updateInventory();
	}

}
