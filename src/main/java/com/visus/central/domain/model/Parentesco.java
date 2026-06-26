package com.visus.central.domain.model;

public enum Parentesco {
	Hijo("Hijo/a"), Esposo("Esposo/a"), Nieto("Nieto/a"), Sobrino("Sobrino/a"), Otros("Otros");

	private final String label;

	Parentesco(String label) {
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
