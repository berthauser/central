package com.visus.central.domain.model;

public enum TipoMovimiento {

	DEBITO("Débito"), CREDITO("Crédito");

	private final String label;

	TipoMovimiento(String label) {
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
