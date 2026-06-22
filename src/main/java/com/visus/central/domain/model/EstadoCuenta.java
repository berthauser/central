package com.visus.central.domain.model;

public enum EstadoCuenta {

	PENDIENTE("Pendiente"), PAGADA("Pagada"), VENCIDA("Vencida"), CANCELADA("Cancelada"),
	NOTA_CREDITO("Nota de Crédito");

	private final String label;

	EstadoCuenta(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static EstadoCuenta fromLabel(String label) {
		for (EstadoCuenta e : values()) {
			if (e.label.equalsIgnoreCase(label)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Estado inválido: " + label);
	}

}
