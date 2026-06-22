package com.visus.central.domain.model;

public enum EstadoComision {
	PENDIENTE("Pendiente"),
	PAGADA("Pagada"),
	AJUSTADA("Ajustada"),
	ANULADA("Anulada");

	private final String label;

	EstadoComision(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static EstadoComision fromLabel(String label) {
		for (EstadoComision e : values()) {
			if (e.label.equalsIgnoreCase(label)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Estado de comisión inválido: " + label);
	}
}
