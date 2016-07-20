package me.herobrinedobem.heventos.api;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.utils.ConfigUtil;
import me.herobrinedobem.heventos.utils.Cuboid;

public class HEventosAPI {

	public static Color getRandomColor(){
		Random r = new Random();
		switch (r.nextInt(17) + 1) {
			case 1:
				return Color.AQUA;
			case 2:
				return Color.BLACK;
			case 3:
				return Color.BLUE;
			case 4:
				return Color.FUCHSIA;
			case 5:
				return Color.GRAY;
			case 6:
				return Color.GREEN;
			case 7:
				return Color.LIME;
			case 8:
				return Color.MAROON;
			case 10:
				return Color.NAVY;
			case 11:
				return Color.OLIVE;
			case 12:
				return Color.ORANGE;
			case 13:
				return Color.PURPLE;
			case 14:
				return Color.RED;
			case 15:
				return Color.SILVER;
			case 16:
				return Color.TEAL;
			case 17:
				return Color.WHITE;
			case 18:
				return Color.YELLOW;
			default:
				return Color.YELLOW;
		}
	}
	
	public static Location getLocation(YamlConfiguration config, final String local) {
		if(config.getString(local).split(";").length == 4){
			String world = config.getString(local).split(";")[0];
			double x = Double.parseDouble(config.getString(local).split(";")[1]);
			double y = Double.parseDouble(config.getString(local).split(";")[2]);
			double z = Double.parseDouble(config.getString(local).split(";")[3]);
			return new Location(HEventos.getHEventos().getServer().getWorld(world), x, y, z);
		}else if(config.getString(local).split(";").length == 6){
			String world = config.getString(local).split(";")[0];
			double x = Double.parseDouble(config.getString(local).split(";")[1]);
			double y = Double.parseDouble(config.getString(local).split(";")[2]);
			double z = Double.parseDouble(config.getString(local).split(";")[3]);
			float yaw = Float.parseFloat(config.getString(local).split(";")[4]);
			float pitch = Float.parseFloat(config.getString(local).split(";")[5]);
			return new Location(HEventos.getHEventos().getServer().getWorld(world), x, y, z, yaw, pitch);
		}else{
			return null;
		}
	}
	
	public static boolean hasEventoOcorrendo(){
		if(HEventos.getHEventos().getEventosController().getEvento() != null){
			return true;
		}else{
			return false;
		}
	}
	
	public static EventoBase getEventoOcorrendo(){
		return HEventos.getHEventos().getEventosController().getEvento();
	}
	
	public static Cuboid getCuboID(Location loc1, Location loc2){
		return new Cuboid(loc1, loc2);
	}
	
	public static ConfigUtil getMessagesConfig(){
		return HEventos.getHEventos().getConfigUtil();
	}
	
	public static ArrayList<EventoBase> getExternalEventos(){
		return HEventos.getHEventos().getExternalEventos();
	}
	
	private static boolean checkItemStacks(final ItemStack[] ises) {
  		for(ItemStack is : ises){
			if(is != null && is.getType() != Material.AIR){
  				return true;
			}
  		}
  		return false;
	}
	
	public static boolean checkInventory(final Player player) {
		if((player != null) && (player.isOnline())) {
			return (checkItemStacks(player.getInventory().getArmorContents()) && checkItemStacks(player.getInventory().getContents()));
		}
		return false;
	}
	
}
