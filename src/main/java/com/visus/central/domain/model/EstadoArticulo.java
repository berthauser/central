package com.visus.central.domain.model;

public enum EstadoArticulo {
	EnExistencias("En Existencias"), Disponible("Disponible"), Comprometido("Comprometido"),
	NoDisponible("No Disponible"), Entrante("Entrante"), Baja("Baja");

	private final String label;

	EstadoArticulo(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public String getLabel() {
		return label;
	}

	public static EstadoArticulo fromLabel(String label) {
		for (EstadoArticulo e : values()) {
			if (e.label.equals(label)) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant " + label);
	}
}
