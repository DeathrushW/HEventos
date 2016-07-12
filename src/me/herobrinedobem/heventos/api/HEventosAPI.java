package me.herobrinedobem.heventos.api;

import java.util.ArrayList;
import org.bukkit.Location;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.utils.ConfigUtil;
import me.herobrinedobem.heventos.utils.Cuboid;

public class HEventosAPI {

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
	
}
