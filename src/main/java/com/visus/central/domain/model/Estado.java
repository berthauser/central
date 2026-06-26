package com.visus.central.domain.model;

public enum Estado {
	Habilitado("Habilitado"), Deshabilitado("Deshabilitado"), Baja("Baja");

	private final String label;

	Estado(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public String getLabel() {
		return label;
	}
}
