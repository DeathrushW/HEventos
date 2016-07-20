package me.herobrinedobem.heventos.api.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.herobrinedobem.heventos.api.EventoBase;

public class EventoPlayerWinEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private EventoBase evento;
	private int position;
	
	public EventoPlayerWinEvent(Player player, EventoBase evento, int position) {
		this.player = player;
		this.evento = evento;
		this.position = position;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
		 
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public EventoBase getEvento() {
		return evento;
	}
	
	public int getPosition() {
		return position;
	}
	
}
