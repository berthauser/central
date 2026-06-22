package com.visus.central.domain.model;

public enum Columna {

	DB("Debe"), CR("Haber");

	private final String label;

	Columna(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
