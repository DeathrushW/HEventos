package me.herobrinedobem.heventos.api.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.herobrinedobem.heventos.api.EventoBase;

public class EventoPlayerOutEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private EventoBase evento;
	private boolean assistindo;
	
	public EventoPlayerOutEvent(Player player, EventoBase evento, boolean assistindo) {
		this.player = player;
		this.evento = evento;
		this.assistindo = assistindo;
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
	
	public boolean isAssistindo() {
		return assistindo;
	}

}
