package com.visus.central.domain.model;

public enum Provincia {
	Buenos_Aires("Buenos Aires"), Catamarca("Catamarca"), Chaco("Chaco"), Chubut("Chubut"), Córdoba("Córdoba"),
	Corrientes("Corrientes"), Entre_Ríos("Entre Ríos"), Formosa("Formosa"), Jujuy("Jujuy"), La_Pampa("La Pampa"),
	La_Rioja("La Rioja"), Mendoza("Mendoza"), Misiones("Misiones"), Neuquén("Neuquén"), Río_Negro("Río Negro"),
	Salta("Salta"), San_Juan("San Juan"), San_Luis("San Luis"), Santa_Cruz("Santa Cruz"), Santa_Fe("Santa Fe"),
	Santiago_del_Estero("Santiago del Estero"), Tierra_del_Fuego("Tierra del Fuego"), Tucumán("Tucumán"),
	Ciudad_Autónoma_de_Buenos_Aires("Ciudad Autónoma de Buenos Aires");

	private final String label;

	Provincia(String label) {
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
