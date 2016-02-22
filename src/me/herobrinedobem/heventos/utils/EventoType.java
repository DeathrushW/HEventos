package me.herobrinedobem.heventos.utils;

public enum EventoType {

	BATATA_QUENTE,
	FROG,
	KILLER,
	MINA_MORTAL,
	SPLEEF,
	PAINTBALL,
	SEMAFORO,
	NORMAL;

	public static EventoType getEventoType(final String type) {
		switch (type) {
			case "batataquente":
				return EventoType.BATATA_QUENTE;
			case "frog":
				return EventoType.FROG;
			case "killer":
				return EventoType.KILLER;
			case "minamortal":
				return EventoType.MINA_MORTAL;
			case "spleef":
				return EventoType.SPLEEF;
			case "paintball":
				return EventoType.PAINTBALL;
			case "semaforo":
				return EventoType.SEMAFORO;
			case "normal":
				return EventoType.NORMAL;
			default:
				return EventoType.NORMAL;
		}
	}

}
