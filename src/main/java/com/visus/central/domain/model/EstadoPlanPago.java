package com.visus.central.domain.model;

public enum EstadoPlanPago {
	
	PENDIENTE("Pendiente"), PAGADA("Pagada"), VENCIDA("Vencida"), CANCELADA("Anulada"),	PARCIAL("Parcial");
	
	private final String label;

	EstadoPlanPago(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static EstadoPlanPago fromLabel(String label) {
		for (EstadoPlanPago e : values()) {
			if (e.label.equalsIgnoreCase(label)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Estado inválido: " + label);
	}

}
