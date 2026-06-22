package com.visus.central.domain.model;

public enum TipoEventoComision {
	FACTURA("Factura Emitida"),
	COBRO("Cobro Registrado");

	private final String label;

	TipoEventoComision(String label) {
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
