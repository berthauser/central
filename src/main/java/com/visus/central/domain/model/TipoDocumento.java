package com.visus.central.domain.model;

public enum TipoDocumento {
	Cuit_Cuil("CUIT/CUIL"), Dni("DNI"), No_Corresponde("No Corresponde");

	private final String label;

	TipoDocumento(String label) {
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
