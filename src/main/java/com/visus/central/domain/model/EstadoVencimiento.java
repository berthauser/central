package com.visus.central.domain.model;

public enum EstadoVencimiento {

	PENDIENTE("Pendiente"), PAGADA("Pagada"), VENCIDA("Vencida");

	private final String label;

	EstadoVencimiento(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static EstadoVencimiento fromLabel(String label) {
		for (EstadoVencimiento e : values()) {
			if (e.label.equalsIgnoreCase(label)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Estado inválido: " + label);
	}

}
