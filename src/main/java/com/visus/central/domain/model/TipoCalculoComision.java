package com.visus.central.domain.model;

public enum TipoCalculoComision {
	PORCENTAJE("Porcentaje"),
	MONTO_FIJO("Monto Fijo"),
	ESCALONADO("Escalonado por Tramos");

	private final String label;

	TipoCalculoComision(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}
}
