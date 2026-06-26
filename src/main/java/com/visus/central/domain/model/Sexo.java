package com.visus.central.domain.model;

public enum Sexo {
	Masculino("Masculino"), Femenino("Femenino"), No_Corresponde("No Corresponde");

	private final String label;

	Sexo(String label) {
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
