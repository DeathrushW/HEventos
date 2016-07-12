package me.herobrinedobem.heventos.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoBase;
import me.herobrinedobem.heventos.api.EventoType;
import me.herobrinedobem.heventos.api.listeners.EventoStartEvent;

public class EventoVerifyHour extends Thread {

	@Override
	public void run() {
		while (true) {
			final Calendar cal = Calendar.getInstance();
			final String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
			final String minutos = String.valueOf(cal.get(Calendar.MINUTE));
			for (final String s : HEventos.getHEventos().getConfig().getStringList("Horarios")) {
				if (s.startsWith(hora + ":" + minutos)) {
					if (HEventos.getHEventos().getEventosController().getEvento() == null) {
						HEventos.getHEventos().getEventosController().setEvento(s.split("-")[1], EventoType.getEventoType(s.split("-")[1]));
						HEventos.getHEventos().getEventosController().getEvento().run();
						EventoStartEvent event = new EventoStartEvent(HEventos.getHEventos().getEventosController().getEvento(), true);
						HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
					}
				} else if (s.startsWith(String.valueOf(cal.get(Calendar.DAY_OF_WEEK)))) {
					if (s.contains(hora + ":" + minutos)) {
						if (HEventos.getHEventos().getEventosController().getEvento() == null) {
							HEventos.getHEventos().getEventosController().setEvento(s.split("-")[1], EventoType.getEventoType(s.split("-")[1]));
							HEventos.getHEventos().getEventosController().getEvento().run();
							EventoStartEvent event = new EventoStartEvent(HEventos.getHEventos().getEventosController().getEvento(), true);
							HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
						}
					}
				}
			}
			for(EventoBase s : HEventos.getHEventos().getExternalEventos()){
				if (s.getHorarioStart().startsWith(hora + ":" + minutos)) {
					if (HEventos.getHEventos().getEventosController().getEvento() == null) {
						HEventos.getHEventos().getEventosController().setEvento(s.getHorarioStart().split("-")[1], EventoType.getEventoType(s.getHorarioStart().split("-")[1]));
						HEventos.getHEventos().getEventosController().getEvento().run();
						EventoStartEvent event = new EventoStartEvent(HEventos.getHEventos().getEventosController().getEvento(), true);
						HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
					}
				}
			}
			try {
				Thread.sleep(10 * 1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public String getData() {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		return sdf.format(new Date());
	}

}
