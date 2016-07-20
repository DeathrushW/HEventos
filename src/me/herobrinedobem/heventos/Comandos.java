package me.herobrinedobem.heventos;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.EventoType;
import me.herobrinedobem.heventos.api.HEventosAPI;
import me.herobrinedobem.heventos.api.listeners.EventoStartEvent;
import me.herobrinedobem.heventos.api.listeners.EventoStopEvent;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerJoinEvent;
import me.herobrinedobem.heventos.api.listeners.EventoPlayerOutEvent;
import me.herobrinedobem.heventos.eventos.BatataQuente;
import me.herobrinedobem.heventos.eventos.BowSpleef;
import me.herobrinedobem.heventos.eventos.EventoNormal;
import me.herobrinedobem.heventos.eventos.Killer;
import me.herobrinedobem.heventos.eventos.MinaMortal;
import me.herobrinedobem.heventos.eventos.Paintball;
import me.herobrinedobem.heventos.eventos.Semaforo;
import me.herobrinedobem.heventos.eventos.Spleef;
import me.herobrinedobem.heventos.utils.EventoCancellType;

public class Comandos implements CommandExecutor {

	private final HEventos instance;

	public Comandos(final HEventos instance) {
		this.instance = instance;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		Player p;
		if (sender instanceof Player) {
			p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("evento")) {
				if ((args.length == 1) && (args[0].equalsIgnoreCase("entrar"))) {
					if (HEventos.getHEventos().getEventosController().getEvento() != null) {
						if (!HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(p.getName())) {
							if (HEventos.getHEventos().getEventosController().getEvento().isAberto()) {
								if (HEventos.getHEventos().getEventosController().getEvento().isVip()) {
									if (p.hasPermission("heventos.vip") || p.hasPermission("heventos.admin")) {
										if (HEventos.getHEventos().getEventosController().getEvento().getEventoType() == EventoType.KILLER) {
											
											if(HEventos.getHEventos().getSc() != null){
												if (HEventos.getHEventos().getSc().getClanManager().getClanPlayer(p) != null) {
													HEventos.getHEventos().getSc().getClanManager().getClanPlayer(p).setFriendlyFire(true);
												}
											}else{
												if(HEventos.getHEventos().getCore() != null){
													if (HEventos.getHEventos().getCore().getClanPlayerManager().getClanPlayer(p) != null) {
														HEventos.getHEventos().getCore().getClanPlayerManager().getClanPlayer(p).setFriendlyFire(true);
													}
												}
											}
										}
										if (HEventos.getHEventos().getEventosController().getEvento().isInventoryEmpty()) {
											if (HEventosAPI.checkInventory(p)) {
												p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgInventarioVazio());
												return false;
											}
										}
										HEventos.getHEventos().getEventosController().getEvento().getParticipantes().add(p.getName());
										p.teleport(HEventos.getHEventos().getEventosController().getEvento().getAguarde());
										for (final String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
											final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
											pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
										}
										EventoPlayerJoinEvent event = new EventoPlayerJoinEvent(p, HEventos.getHEventos().getEventosController().getEvento(), false);
										HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
										return true;
									} else {
										p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoVip());
										return true;
									}
								} else {
									if (HEventos.getHEventos().getEventosController().getEvento().getEventoType() == EventoType.KILLER) {
										if(HEventos.getHEventos().getSc() != null){
											if (HEventos.getHEventos().getSc().getClanManager().getClanPlayer(p) != null) {
												HEventos.getHEventos().getSc().getClanManager().getClanPlayer(p).setFriendlyFire(true);
											}
										}else{
											if(HEventos.getHEventos().getCore() != null){
												if (HEventos.getHEventos().getCore().getClanPlayerManager().getClanPlayer(p) != null) {
													HEventos.getHEventos().getCore().getClanPlayerManager().getClanPlayer(p).setFriendlyFire(true);
												}
											}
										}
									}
									if (HEventos.getHEventos().getEventosController().getEvento().isInventoryEmpty()) {
										if (HEventosAPI.checkInventory(p)) {
											p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgInventarioVazio());
											return false;
										}
									}
									HEventos.getHEventos().getEventosController().getEvento().getParticipantes().add(p.getName());
									p.teleport(HEventos.getHEventos().getEventosController().getEvento().getAguarde());
									for (final String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
									}
									return true;
								}
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoFechado());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("sair"))) {
					if (HEventos.getHEventos().getEventosController().getEvento() != null) {
						if (HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(p.getName())) {
							for (final String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
								final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
								pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
							}
							HEventos.getHEventos().getEventosController().getEvento().getParticipantes().remove(p.getName());
							p.teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
							EventoPlayerOutEvent event = new EventoPlayerOutEvent(p, HEventos.getHEventos().getEventosController().getEvento(), false);
							HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
							return true;
						} else {
							if (HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
								HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().remove(p.getName());
								p.teleport(HEventos.getHEventos().getEventosController().getEvento().getSaida());
								p.setAllowFlight(false);
								p.setFlying(false);
								for (final String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.showPlayer(p);
								}
								for (final String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								}
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								EventoPlayerOutEvent event = new EventoPlayerOutEvent(p, HEventos.getHEventos().getEventosController().getEvento(), true);
								HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
								return true;
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNaoParticipa());
								return true;
							}
						}
					}
				} else if ((args.length == 3) && (args[0].equalsIgnoreCase("iniciar"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().getEvento() == null) {
							if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
								if(HEventos.getHEventos().getEventosController().externalEvento(args[1])){
									return true;
								}
								if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
									switch (EventoType.getEventoType(args[1])) {
										case SPLEEF:
											final Spleef spleef = new Spleef(HEventos.getHEventos().getEventosController().getConfigFile(args[1]));
											HEventos.getHEventos().getEventosController().setEvento(spleef);
											HEventos.getHEventos().getEventosController().getEvento().setVip(Boolean.parseBoolean(args[2]));
											HEventos.getHEventos().getEventosController().getEvento().run();
											break;
										case BOW_SPLEEF:
											final BowSpleef bowspleef = new BowSpleef(HEventos.getHEventos().getEventosController().getConfigFile(args[1]));
											HEventos.getHEventos().getEventosController().setEvento(bowspleef);
											HEventos.getHEventos().getEventosController().getEvento().setVip(Boolean.parseBoolean(args[2]));
											HEventos.getHEventos().getEventosController().getEvento().run();
											break;
										case BATATA_QUENTE:
											final BatataQuente batata = new BatataQuente(HEventos.getHEventos().getEventosController().getConfigFile(args[1]));
											HEventos.getHEventos().getEventosController().setEvento(batata);
											HEventos.getHEventos().getEventosController().getEvento().setVip(Boolean.parseBoolean(args[2]));
											HEventos.getHEventos().getEventosController().getEvento().run();
											break;
										case KILLER:
											final Killer killer = new Killer(HEventos.getHEventos().getEventosController().getConfigFile(args[1]));
											HEventos.getHEventos().getEventosController().setEvento(killer);
											HEventos.getHEventos().getEventosController().getEvento().setVip(Boolean.parseBoolean(args[2]));
											HEventos.getHEventos().getEventosController().getEvento().run();
											break;
										case MINA_MORTAL:
											final MinaMortal mina = new MinaMortal(HEventos.getHEventos().getEventosController().getConfigFile(args[1]));
											HEventos.getHEventos().getEventosController().setEvento(mina);
											HEventos.getHEventos().getEventosController().getEvento().setVip(Boolean.parseBoolean(args[2]));
											HEventos.getHEventos().getEventosController().getEvento().run();
											break;
										case PAINTBALL:
											final Paintball paint = new Paintball(HEventos.getHEventos().getEventosController().getConfigFile(args[1]));
											HEventos.getHEventos().getEventosController().setEvento(paint);
											HEventos.getHEventos().getEventosController().getEvento().setVip(Boolean.parseBoolean(args[2]));
											HEventos.getHEventos().getEventosController().getEvento().run();
											break;
										case SEMAFORO:
											final Semaforo semaforo = new Semaforo(HEventos.getHEventos().getEventosController().getConfigFile(args[1]));
											HEventos.getHEventos().getEventosController().setEvento(semaforo);
											HEventos.getHEventos().getEventosController().getEvento().setVip(Boolean.parseBoolean(args[2]));
											HEventos.getHEventos().getEventosController().getEvento().run();
											break;
										case NORMAL:
											final EventoNormal evento = new EventoNormal(HEventos.getHEventos().getEventosController().getConfigFile(args[1]));
											HEventos.getHEventos().getEventosController().setEvento(evento);
											HEventos.getHEventos().getEventosController().getEvento().setVip(Boolean.parseBoolean(args[2]));
											HEventos.getHEventos().getEventosController().getEvento().run();
											break;
										default:
											final EventoNormal evento2 = new EventoNormal(HEventos.getHEventos().getEventosController().getConfigFile(args[1]));
											HEventos.getHEventos().getEventosController().setEvento(evento2);
											HEventos.getHEventos().getEventosController().getEvento().setVip(Boolean.parseBoolean(args[2]));
											HEventos.getHEventos().getEventosController().getEvento().run();
											break;
									}
									EventoStartEvent event = new EventoStartEvent(HEventos.getHEventos().getEventosController().getEvento(), false);
									HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
									p.sendMessage("§4[Evento] §cEvento iniciado com sucesso!");
									return true;
								} else {
									p.sendMessage("§4[Evento] §cEvento nao encontrado!");
									return true;
								}
							} else {
								p.sendMessage("§4[Evento] §cUtilize /evento iniciar <nome> <true/false>");
								return true;
							}
						} else {
							p.sendMessage("§4[Evento] §cJa existe um evento ocorrendo no momento!");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("iniciar"))) {
					p.sendMessage("§4[Evento] §cUtilize /evento iniciar <nome> <true/false>");
					return true;
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("cancelar"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().getEvento() != null) {
							EventoStopEvent event = new EventoStopEvent(HEventos.getHEventos().getEventosController().getEvento(), EventoCancellType.CANCELLED);
							HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
							
							HEventos.getHEventos().getEventosController().getEvento().cancelEvent();
							p.sendMessage("§4[Evento] §cEvento cancelado com sucesso!");
							return true;
						} else {
							p.sendMessage("§4[Evento] §cNao existe um evento ocorrendo no momento!");
							return true;
						}
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("iniciar"))) {
					if (p.hasPermission("heventos.admin")) {
						p.sendMessage("§4[Evento] §cUtilize /evento iniciar <nome> <true/false>");
						return true;
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("assistir"))) {
					if (HEventos.getHEventos().getEventosController().getEvento() != null) {
						if (HEventos.getHEventos().getEventosController().getEvento().isAssistirAtivado()) {
							if (!HEventos.getHEventos().getEventosController().getEvento().getParticipantes().contains(p.getName())) {
								if (!HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().contains(p.getName())) {
									HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers().add(p.getName());
									if (HEventos.getHEventos().getEventosController().getEvento().isAberto()) {
										p.teleport(HEventos.getHEventos().getEventosController().getEvento().getAguarde());
									} else {
										p.teleport(HEventos.getHEventos().getEventosController().getEvento().getEntrada());
									}
									for (final String s : HEventos.getHEventos().getEventosController().getEvento().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
									}
									for (final String s : HEventos.getHEventos().getEventosController().getEvento().getCamarotePlayers()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
									}
									p.setAllowFlight(true);
									p.setFlying(true);
									p.setFlySpeed(1 / 10.0f);
									EventoPlayerJoinEvent event = new EventoPlayerJoinEvent(p, HEventos.getHEventos().getEventosController().getEvento(), true);
									HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
								} else {
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaEstaCamarote());
									return true;
								}
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistirDesativado());
							return true;
						}
					} else {
						p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNenhumEvento());
						return true;
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("multiplicador"))) {
					if (p.hasPermission("heventos.admin")) {
						p.sendMessage("§4[Evento] §cUtilize /evento multiplicador <valor>");
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("multiplicador"))) {
					if (p.hasPermission("heventos.admin")) {
						try {
							this.instance.getConfig().set("Money_Multiplicador", Integer.parseInt(args[1]));
							this.instance.saveConfig();
							p.sendMessage("§4[Evento] §cRate alterado com sucesso!");
							HEventos.getHEventos().getServer().broadcastMessage("§4[Eventos] §cMultiplicador de money dos eventos alterado para §4" + args[1] + "*");
							return true;
						} catch (final NumberFormatException e) {
							p.sendMessage("§4[Evento] §cUtilize apenas numeros no rate.");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("multiplicador")) && (args[1].equalsIgnoreCase("reset"))) {
					if (p.hasPermission("heventos.admin")) {
						this.instance.getConfig().set("Money_Multiplicador", 1);
						this.instance.saveConfig();
						p.sendMessage("§4[Evento] §cRate alterado com sucesso!");
						HEventos.getHEventos().getServer().broadcastMessage("  ");
						HEventos.getHEventos().getServer().broadcastMessage("§4[Eventos] §cRate de money dos eventos voltou ao normal!");
						HEventos.getHEventos().getServer().broadcastMessage("  ");
						return true;
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("top"))) {
					HEventos.getHEventos().getDatabaseManager().getTOPWins(p);
					if(args[1].equalsIgnoreCase("participacoes")){
						HEventos.getHEventos().getDatabaseManager().getTOPParticipations(p);
						return true;
					}else if(args[1].equalsIgnoreCase("vencedores")){
						HEventos.getHEventos().getDatabaseManager().getTOPWins(p);
						return true;
					}else{
						for (final String s : HEventos.getHEventos().getConfig().getStringList("Mensagens.Default")) {
							p.sendMessage(s.replace("&", "§"));
						}
						return true;
					}
				}else if ((args.length == 2) && (args[0].equalsIgnoreCase("setentrada"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
							final EventoBase evento = HEventos.getHEventos().getEventosController().loadEvento(args[1]);
							evento.getConfig().set("Localizacoes.Entrada", this.getLocationForConfig(p.getLocation()));
							final File file = new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + args[1] + ".yml");
							try {
								evento.getConfig().save(file);
							} catch (final IOException e) {
								e.printStackTrace();
							}
							p.sendMessage("§4[Evento] §cEntrada do evento " + args[1] + " setada com sucesso!");
						} else {
							p.sendMessage("§4[Evento] §cEvento nao encontrado na pasta.");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("setsaida"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
							final EventoBase evento = HEventos.getHEventos().getEventosController().loadEvento(args[1]);
							evento.getConfig().set("Localizacoes.Saida", this.getLocationForConfig(p.getLocation()));
							final File file = new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + args[1] + ".yml");
							try {
								evento.getConfig().save(file);
							} catch (final IOException e) {
								e.printStackTrace();
							}
							p.sendMessage("§4[Evento] §cSaida do evento " + args[1] + " setada com sucesso!");
						} else {
							p.sendMessage("§4[Evento] §cEvento nao encontrado na pasta.");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("setcamarote"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
							final EventoBase evento = HEventos.getHEventos().getEventosController().loadEvento(args[1]);
							evento.getConfig().set("Localizacoes.Camarote", this.getLocationForConfig(p.getLocation()));
							final File file = new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + args[1] + ".yml");
							try {
								evento.getConfig().save(file);
							} catch (final IOException e) {
								e.printStackTrace();
							}
							p.sendMessage("§4[Evento] §cCamarote do evento " + args[1] + " setado com sucesso!");
						} else {
							p.sendMessage("§4[Evento] §cEvento nao encontrado na pasta.");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("setaguardando"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
							final EventoBase evento = HEventos.getHEventos().getEventosController().loadEvento(args[1]);
							evento.getConfig().set("Localizacoes.Aguardando", this.getLocationForConfig(p.getLocation()));
							final File file = new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + args[1] + ".yml");
							try {
								evento.getConfig().save(file);
							} catch (final IOException e) {
								e.printStackTrace();
							}
							p.sendMessage("§4[Evento] §cLocal de espera do evento " + args[1] + " setado com sucesso!");
						} else {
							p.sendMessage("§4[Evento] §cEvento nao encontrado na pasta.");
							return true;
						}
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("reload"))) {
					if (p.hasPermission("heventos.admin")) {
						HEventos.getHEventos().reloadConfig();
						HEventos.getHEventos().getConfigUtil().setupConfigUtils();
						p.sendMessage("§4[Evento] §cConfiguraçao recarregada com sucesso!");
						return true;
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("lista"))) {
					if (p.hasPermission("heventos.admin")) {
						final StringBuilder builder = new StringBuilder();
						for (final File file : new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/").listFiles()) {
							builder.append("§6" + file.getName().replace(".yml", "") + " §0- ");
						}
						p.sendMessage("§4[Evento] §cLista de eventos:");
						p.sendMessage(builder.toString());
						return true;
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("tool"))) {
					if (p.hasPermission("heventos.admin")) {
						if (args[1].equalsIgnoreCase("spleef")) {
							final ItemStack item = new ItemStack(Material.IRON_AXE, 1);
							final ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§4§lEvento Spleef");
							meta.setLore(Arrays.asList("§6* Clique com o botao direito para marcar a posicao 1 do chao", "§6* Clique com o botao esquerdo para marcar a posicao 2 do chao"));
							item.setItemMeta(meta);
							p.getInventory().addItem(item);
							p.updateInventory();
							return true;
						} else if (args[1].equalsIgnoreCase("minamortal")) {
							final ItemStack item = new ItemStack(Material.IRON_AXE, 1);
							final ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§4§lEvento MinaMortal");
							meta.setLore(Arrays.asList("§6* Clique com o botao direito para marcar a posicao 1 da mina", "§6* Clique com o botao esquerdo para marcar a posicao 2 da mina"));
							item.setItemMeta(meta);
							p.getInventory().addItem(item);
							p.updateInventory();
							return true;
						} else if (args[1].equalsIgnoreCase("bowspleef")) {
							final ItemStack item = new ItemStack(Material.IRON_AXE, 1);
							final ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§4§lEvento BowSpleef");
							meta.setLore(Arrays.asList("§6* Clique com o botao direito para marcar a posicao 1 do chao", "§6* Clique com o botao esquerdo para marcar a posicao 2 do chao"));
							item.setItemMeta(meta);
							p.getInventory().addItem(item);
							p.updateInventory();
							return true;
						} else if (args[1].equalsIgnoreCase("paintball")) {
							final ItemStack item = new ItemStack(Material.IRON_AXE, 1);
							final ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§4§lEvento Paintball");
							meta.setLore(Arrays.asList("§6* Clique com o botao direito para marcar a posicao 1 do chao", "§6* Clique com o botao esquerdo para marcar a posicao 2 do chao"));
							item.setItemMeta(meta);
							p.getInventory().addItem(item);
							p.updateInventory();
							return true;
						} else {
							p.sendMessage("§4[Evento] §cUtilize /evento tool <spleef/minamortal/bowspleef/paintball>");
							return true;
						}
					}
				} else {
					for (final String s : HEventos.getHEventos().getConfig().getStringList("Mensagens.Default")) {
						p.sendMessage(s.replace("&", "§"));
					}
					if (p.hasPermission("heventos.admin")) {
						p.sendMessage("§4/evento iniciar <nome> §c- Inicia um evento");
						p.sendMessage("§4/evento cancelar §c- Cancela um evento");
						p.sendMessage("§4/evento setentrada <evento> §c- Seta a entrada de um evento");
						p.sendMessage("§4/evento setsaida <evento> §c- Seta a saida de um evento");
						p.sendMessage("§4/evento setcamarote <evento> §c- Seta o camarote de um evento");
						p.sendMessage("§4/evento setaguardando <evento> §c- Seta local de espera de um evento");
						p.sendMessage("§4/evento multiplicador §c- Altera o multiplicador de money");
						p.sendMessage("§4/evento multiplicador reset §c- Reseta o multiplicador");
						p.sendMessage("§4/evento reload §c- Recarrega a config do plugin");
						p.sendMessage("§4/evento tool <evento> §c- Pega uma ferramenta para setar locs");
						p.sendMessage("§4/evento lista §c- Mostra a lista de eventos");
					}
				}

			}

		}
		return true;
	}

	private String getLocationForConfig(final Location loc) {
		final String world = loc.getWorld().getName();
		final int x = (int) loc.getX();
		final int y = (int) loc.getY();
		final int z = (int) loc.getZ();
		final float yaw = (float) loc.getYaw();
		final float pitch = (float) loc.getPitch();
		return world + ";" + String.valueOf(x) + ";" + String.valueOf(y) + ";" + String.valueOf(z) + ";" + String.valueOf(yaw) + ";" + String.valueOf(pitch);
	}

}
