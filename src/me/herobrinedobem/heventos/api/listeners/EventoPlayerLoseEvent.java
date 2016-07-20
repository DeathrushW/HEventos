package me.herobrinedobem.heventos.api.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.herobrinedobem.heventos.api.EventoBase;

public class EventoPlayerLoseEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private EventoBase evento;
	
	public EventoPlayerLoseEvent(Player player, EventoBase evento) {
		this.player = player;
		this.evento = evento;
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
	
}
