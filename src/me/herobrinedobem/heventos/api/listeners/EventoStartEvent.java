package me.herobrinedobem.heventos.api.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import me.herobrinedobem.heventos.api.EventoBase;

public class EventoStartEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private EventoBase evento;
	private boolean automaticStart;
	
	public EventoStartEvent(EventoBase evento, boolean automaticStart) {
		this.evento = evento;
		this.automaticStart = automaticStart;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
		 
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public EventoBase getEvento() {
		return evento;
	}
	
	public boolean isAutomaticStart() {
		return automaticStart;
	}
	
}
