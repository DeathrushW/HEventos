package me.herobrinedobem.heventos;

import java.io.File;
import java.io.IOException;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerLoseEvent;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerWinEvent;

public class MainListeners implements Listener {
	
	@EventHandler
	private void onEventoPlayerLoseEvent(final EventoPlayerLoseEvent e){
		if(e.getEvento().isInventoryEmpty()){
			e.getPlayer().getInventory().setHelmet(null);
			e.getPlayer().getInventory().setChestplate(null);
			e.getPlayer().getInventory().setLeggings(null);
			e.getPlayer().getInventory().setBoots(null);
			e.getPlayer().getInventory().clear();
		}
		e.getEvento().getParticipantes().remove(e.getPlayer().getName());
		e.getPlayer().teleport(e.getEvento().getSaida());
	}
	
	@EventHandler
	private void onEventoPlayerWinEvent(final EventoPlayerWinEvent e){
		if(e.getEvento().isInventoryEmpty()){
			e.getPlayer().getInventory().setHelmet(null);
			e.getPlayer().getInventory().setChestplate(null);
			e.getPlayer().getInventory().setLeggings(null);
			e.getPlayer().getInventory().setBoots(null);
			e.getPlayer().getInventory().clear();
		}
		if(e.getEvento().isContarVitoria()){
			HEventos.getHEventos().getDatabaseManager().addWinPoint(e.getPlayer().getName(), 1);
		}
		if(e.getEvento().isContarParticipacao()){
			HEventos.getHEventos().getDatabaseManager().addParticipationPoint(e.getPlayer().getName(), 1);
		}
		if(e.getPosition() == 0){
			for(String comando : e.getEvento().getConfig().getStringList("Premios.Comandos")){
				HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), comando.replace("$player$", e.getPlayer().getName()));
			}
			HEventos.getHEventos().getEconomy().depositPlayer(e.getPlayer().getName(), e.getEvento().getConfig().getDouble("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador"));
			for (final String s : e.getEvento().getConfig().getStringList("Mensagens.Vencedor")) {
				HEventos.getHEventos().getServer().broadcastMessage(s.replaceAll("&", "§").replace("$player$", e.getPlayer().getName()));
			}
		}else{
			for(String comando : e.getEvento().getConfig().getStringList("Premios." + e.getPosition() + "_Lugar.Comandos")){
				HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), comando.replace("$player$", e.getPlayer().getName()));
			}
			HEventos.getHEventos().getEconomy().depositPlayer(e.getPlayer().getName(), e.getEvento().getConfig().getDouble("Premios." + e.getPosition() + "_Lugar.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador"));
			for (final String sa : e.getEvento().getConfig().getStringList("Mensagens.Lugar")) {
				HEventos.getHEventos().getServer().broadcastMessage(sa.replace("&", "§").replace("$player$", e.getPlayer().getName()).replace("$posicao$", e.getPosition() + "°"));
			}
		}
		e.getEvento().getParticipantes().remove(e.getPlayer().getName());
		e.getPlayer().teleport(e.getEvento().getSaida());
	}

	@EventHandler
	private void onPlayerInteractEvent(final PlayerInteractEvent e) {
		if ((e.getPlayer().getItemInHand().getType() == Material.IRON_AXE) && e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().hasDisplayName() && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Evento Spleef")) {
			if (e.getPlayer().hasPermission("heventos.admin")) {
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/spleef.yml");
					final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
					configEvento.set("Localizacoes.Chao_1", e.getClickedBlock().getWorld().getName() + ";" + (int) e.getClickedBlock().getLocation().getX() + ";" + (int) e.getClickedBlock().getLocation().getY() + ";" + (int) e.getClickedBlock().getLocation().getZ());
					try {
						configEvento.save(new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + "spleef.yml"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
					e.getPlayer().sendMessage("§4[Evento] §cLocalizacao 1 do chao do spleef setada!");
					e.setCancelled(true);
				} else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/spleef.yml");
					final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
					configEvento.set("Localizacoes.Chao_2", e.getClickedBlock().getWorld().getName() + ";" + (int) e.getClickedBlock().getLocation().getX() + ";" + (int) e.getClickedBlock().getLocation().getY() + ";" + (int) e.getClickedBlock().getLocation().getZ());
					try {
						configEvento.save(new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + "spleef.yml"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
					e.getPlayer().sendMessage("§4[Evento] §cLocalizacao 2 do chao do spleef setada!");
					e.setCancelled(true);
				}
			}
		} else if ((e.getPlayer().getItemInHand().getType() == Material.IRON_AXE) && e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().hasDisplayName() && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Evento MinaMortal")) {
			if (e.getPlayer().hasPermission("heventos.admin")) {
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/minamortal.yml");
					final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
					configEvento.set("Localizacoes.Mina_1", e.getClickedBlock().getWorld().getName() + ";" + (int) e.getClickedBlock().getLocation().getX() + ";" + (int) e.getClickedBlock().getLocation().getY() + ";" + (int) e.getClickedBlock().getLocation().getZ());
					try {
						configEvento.save(new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + "minamortal.yml"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
					e.getPlayer().sendMessage("§4[Evento] §cLocalizacao 1 da mina setada!");
					e.setCancelled(true);
				} else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/minamortal.yml");
					final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
					configEvento.set("Localizacoes.Mina_2", e.getClickedBlock().getWorld().getName() + ";" + (int) e.getClickedBlock().getLocation().getX() + ";" + (int) e.getClickedBlock().getLocation().getY() + ";" + (int) e.getClickedBlock().getLocation().getZ());
					try {
						configEvento.save(new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + "minamortal.yml"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
					e.getPlayer().sendMessage("§4[Evento] §cLocalizacao 2 da mina setada!");
					e.setCancelled(true);
				}
			}
		} else if ((e.getPlayer().getItemInHand().getType() == Material.IRON_AXE) && e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().hasDisplayName() && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("BowSpleef")) {
			if (e.getPlayer().hasPermission("heventos.admin")) {
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/bowspleef.yml");
					final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
					configEvento.set("Localizacoes.Chao_1", e.getClickedBlock().getWorld().getName() + ";" + (int) e.getClickedBlock().getLocation().getX() + ";" + (int) e.getClickedBlock().getLocation().getY() + ";" + (int) e.getClickedBlock().getLocation().getZ());
					try {
						configEvento.save(new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + "bowspleef.yml"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
					e.getPlayer().sendMessage("§4[Evento] §cLocalizacao 1 do bowspleef setada!");
					e.setCancelled(true);
				} else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/bowspleef.yml");
					final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
					configEvento.set("Localizacoes.Chao_2", e.getClickedBlock().getWorld().getName() + ";" + (int) e.getClickedBlock().getLocation().getX() + ";" + (int) e.getClickedBlock().getLocation().getY() + ";" + (int) e.getClickedBlock().getLocation().getZ());
					try {
						configEvento.save(new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + "bowspleef.yml"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
					e.getPlayer().sendMessage("§4[Evento] §cLocalizacao 2 do bowspleef setada!");
					e.setCancelled(true);
				}
			}
		} else if ((e.getPlayer().getItemInHand().getType() == Material.IRON_AXE) && e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().hasDisplayName() && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Paintball")) {
			if (e.getPlayer().hasPermission("heventos.admin")) {
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/paintball.yml");
					final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
					configEvento.set("Localizacoes.Pos_1", e.getClickedBlock().getWorld().getName() + ";" + (int) e.getClickedBlock().getLocation().getX() + ";" + (int) e.getClickedBlock().getLocation().getY() + ";" + (int) e.getClickedBlock().getLocation().getZ());
					try {
						configEvento.save(new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + "paintball.yml"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
					e.getPlayer().sendMessage("§4[Evento] §cLocalizacao 1 do paintball setada!");
					e.setCancelled(true);
				} else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/paintball.yml");
					final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
					configEvento.set("Localizacoes.Pos_2", e.getClickedBlock().getWorld().getName() + ";" + (int) e.getClickedBlock().getLocation().getX() + ";" + (int) e.getClickedBlock().getLocation().getY() + ";" + (int) e.getClickedBlock().getLocation().getZ());
					try {
						configEvento.save(new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + "paintball.yml"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
					e.getPlayer().sendMessage("§4[Evento] §cLocalizacao 2 do paintball setada!");
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onSignChangeEvent(final SignChangeEvent e) {
		if (e.getPlayer().hasPermission("heventos.admin")) {
			if (e.getLine(0).equalsIgnoreCase("[Evento]")) {
				e.setLine(0, "§9[Evento]");
				e.setLine(1, "§61 Lugar");
				e.setLine(2, "§62 Lugar");
				e.setLine(3, "§63 Lugar");
				e.getPlayer().sendMessage("§4[Evento] §cPlaca criada com sucesso!");
			}
		}
	}

}
