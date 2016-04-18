package me.herobrinedobem.heventos.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import me.herobrinedobem.heventos.HEventos;

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
					}
				} else if (s.startsWith(String.valueOf(cal.get(Calendar.DAY_OF_WEEK)))) {
					if (s.contains(hora + ":" + minutos)) {
						if (HEventos.getHEventos().getEventosController().getEvento() == null) {
							HEventos.getHEventos().getEventosController().setEvento(s.split("-")[1], EventoType.getEventoType(s.split("-")[1]));
							HEventos.getHEventos().getEventosController().getEvento().run();
						}
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
